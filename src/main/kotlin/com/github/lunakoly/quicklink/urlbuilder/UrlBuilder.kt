package com.github.lunakoly.quicklink.urlbuilder

import com.github.lunakoly.quicklink.repository.RepositoryInfo

interface UrlBuilder {
    fun buildUrl(
        remoteLink: String,
        repositoryInfo: RepositoryInfo,
        filePath: String,
        lineNumber: Int,
    ): String
}
