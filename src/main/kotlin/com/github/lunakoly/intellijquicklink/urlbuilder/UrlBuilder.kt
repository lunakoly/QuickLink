package com.github.lunakoly.intellijquicklink.urlbuilder

interface UrlBuilder {
    fun buildUrl(
        remoteLink: String,
        branch: String,
        filePath: String,
        lineNumber: Int,
    ): String
}
