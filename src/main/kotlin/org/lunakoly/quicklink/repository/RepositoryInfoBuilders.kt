package org.lunakoly.quicklink.repository

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.config.GitExecutableManager
import git4idea.history.GitHistoryUtils
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager
import org.lunakoly.quicklink.ui.warn
import org.lunakoly.quicklink.utils.PopupException

class InvalidRemotesException : PopupException(
    "No valid remotes found",
    "No Remotes",
)

class NoRevisionException : PopupException(
    "Could not determine the active revision (a commit or a branch)",
    "No Active Revision",
)

class GitRepositoryNeededException : PopupException(
    "This project has no associate Git repository",
    "No Git Repository Found",
)

fun getCurrentCommitRepositoryInfo(repo: GitRepository): RepositoryInfo =
    getRepositoryInfo(repo) { repo.currentRevision }

fun getLatestDefaultRepositoryInfo(repo: GitRepository, project: Project, remote: String): RepositoryInfo =
    getRepositoryInfo(repo) {
        val defaultBranch = detectDefaultBranchFor(remote, repo, project)
        GitHistoryUtils.collectTimedCommits(project, repo.root, "$remote/$defaultBranch").firstOrNull()?.id?.asString()
    }

fun getReasonablePublishedRepositoryInfo(repo: GitRepository, project: Project, remote: String): RepositoryInfo =
    getRepositoryInfo(repo) {
        val remoteBranch = repo.currentBranch?.findTrackedBranch(repo)?.takeIf { it.remote.name == remote }
        val commitHash = repo.currentRevision ?: throw NoRevisionException()

        val publishedCommit = when {
            remoteBranch != null -> GitHistoryUtils.getMergeBase(project, repo.root, remoteBranch.name, commitHash)?.rev
            else -> findClosestDefaultBranchCommitTo(commitHash, repo, project, remote)
        }

        if (publishedCommit != commitHash) {
            val commits = GitHistoryUtils.collectCommitsMetadata(project, repo.root, commitHash, publishedCommit)
            val current = commits?.first()?.subject?.let { " (${commitHash.take(8)} \"$it\")" } ?: ""
            val published = commits?.last()?.subject?.let { " (${publishedCommit?.take(8)} \"$it\")" } ?: ""
            project.warn("The current commit$current is not available at the remote, picking the closest parent instead$published")
        }

        publishedCommit
    }

fun getRepositoryInfo(repo: GitRepository, getCommitHash: () -> String?): RepositoryInfo {
    val branch = repo.currentBranch?.name
    val commitHash = getCommitHash() ?: throw NoRevisionException()
    return RepositoryInfo(repo.root, branch, commitHash)
}

fun GitRepository.getRemotesMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()

    remotes.forEach {
        val url = it.firstUrl
        val name = it.name

        if (url != null) {
            map[name] = url
        }
    }

    if (map.isEmpty()) {
        throw InvalidRemotesException()
    }

    return map
}

fun findClosestDefaultBranchCommitTo(commitHash: String, repo: GitRepository, project: Project, remote: String): String {
    val git = GitExecutableManager.getInstance().getExecutable(project)

    val isPresentOnRemote = listOf(git.exePath, "branch", "-r", "--contains", commitHash)
        .let(::GeneralCommandLine).withWorkDirectory(repo.root.path)
        .let(::CapturingProcessHandler).runProcess()
        .stdout.split("\n").any { it.startsWith("$remote/") }

    if (isPresentOnRemote) {
        return commitHash
    }

    val defaultBranch = detectDefaultBranchFor(remote, repo, project)
    val branch = repo.branches.findRemoteBranch("$remote/$defaultBranch")
        ?: return commitHash

    return GitHistoryUtils.getMergeBase(project, repo.root, branch.name, commitHash)?.rev ?: commitHash
}

private fun detectDefaultBranchFor(remote: String, repo: GitRepository, project: Project): String {
    val git = GitExecutableManager.getInstance().getExecutable(project)

    return listOf(git.exePath, "symbolic-ref", "refs/remotes/origin/HEAD")
        .let(::GeneralCommandLine).withWorkDirectory(repo.root.path)
        .let(::CapturingProcessHandler).runProcess()
        .stdout.trim().removePrefix("refs/remotes/$remote/")
}

fun getRepositoryFor(file: VirtualFile, project: Project): GitRepository {
    // a project can have multiple repositories reported, if there are
    // submodules in it. info about each of those repositories will be
    // fetched and returned
    val repositories = GitRepositoryManager.getInstance(project).repositories

    if (repositories.isEmpty()) {
        throw GitRepositoryNeededException()
    }

    return repositories.sortedByDescending {
        // sort in descending order by the root path so that the first match will be
        // the nearest parent of the given file
        it.root.path.length
    }.find {
        // returns the repository in which the file is a part of. It can
        // either be the submodule or the original repository
        file.path.startsWith(it.root.path)
    } ?: repositories[0]
}
