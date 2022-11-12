package com.github.lunakoly.quicklink.repository

import com.github.lunakoly.quicklink.utils.PopupException
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

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

fun getRepositoryInfoAsGit(repo: GitRepository): RepositoryInfo {
    val map = mutableMapOf<String, String>()

    repo.remotes.forEach {
        val url = it.firstUrl
        val name = it.name

        if (url != null) {
            map[name] = url
        }
    }

    if (map.isEmpty()) {
        throw InvalidRemotesException()
    }

    val branch = repo.currentBranch?.name

    val commitHash = repo.currentRevision
        ?: throw NoRevisionException()

    return RepositoryInfo(repo.root, branch, commitHash, map)
}

fun getRepositoryInfo(project: Project, file: VirtualFile): RepositoryInfo {
    // a project can have multiple repositories reported, if there are
    // submodules in it. info about each of those repositories will be
    // fetched and returned
    val repositories = GitRepositoryManager.getInstance(project)
        .repositories.map { getRepositoryInfoAsGit(it) }
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
