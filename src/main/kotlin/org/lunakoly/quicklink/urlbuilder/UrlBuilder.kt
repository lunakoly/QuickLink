package org.lunakoly.quicklink.urlbuilder

import org.lunakoly.quicklink.repository.RepositoryInfo

interface UrlBuilder {
    fun buildUrl(
        remoteLink: String,
        repositoryInfo: RepositoryInfo,
        filePath: String,
        selection: Selection,
    ): String
}

data class LineOffset(val lineNumber: Int, val columnNumber: Int)

sealed class Selection {
    data class MultilineSelection(val start: LineOffset, val end: LineOffset) : Selection()
    data class SingleLinkSelection(val lineOffset: LineOffset) : Selection()
}
