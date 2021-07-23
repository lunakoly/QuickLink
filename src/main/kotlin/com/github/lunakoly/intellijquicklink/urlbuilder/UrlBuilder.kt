package com.github.lunakoly.intellijquicklink.urlbuilder

import com.github.lunakoly.intellijquicklink.repository.RepositoryInfo

interface UrlBuilder {
    fun buildUrl(
        remoteLink: String,
        repositoryInfo: RepositoryInfo,
        filePath: String,
        lineNumber: Int,
    ): String
}
