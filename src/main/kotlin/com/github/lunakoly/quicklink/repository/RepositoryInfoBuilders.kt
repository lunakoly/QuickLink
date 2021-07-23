package com.github.lunakoly.quicklink.repository

import com.github.lunakoly.quicklink.utils.PopupException
import com.intellij.openapi.project.Project
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

    return RepositoryInfo(branch, commitHash, map)
}

fun getRepositoryInfo(project: Project): RepositoryInfo {
    GitRepositoryManager.getInstance(project)
        .repositories.firstOrNull()
        ?.let {
            return getRepositoryInfoAsGit(it)
        }

    throw GitRepositoryNeededException()
}
