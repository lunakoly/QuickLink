package org.lunakoly.quicklink.urlbuilder

import org.lunakoly.quicklink.repository.RepositoryInfo

interface UrlBuilder {
    fun buildUrl(
        remoteLink: String,
        repositoryInfo: RepositoryInfo,
        filePath: String,
        lineNumber: Int,
    ): String
}
