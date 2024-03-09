package org.lunakoly.quicklink.urlbuilder

import org.lunakoly.quicklink.repository.RepositoryInfo

interface UrlBuilder {
    fun buildUrl(
        remoteLink: String,
        repositoryInfo: RepositoryInfo,
        filePath: String,
        lineNumber: Pair<Int, Int>,
        columnNumber: Pair<Int, Int>,
        // when isSelection is false, only first item of lineNumber will be used
        // this means we are generating link for a single line
        isSelection: Boolean,
    ): String
}
