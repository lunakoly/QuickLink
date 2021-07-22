package com.github.lunakoly.intellijquicklink.urlbuilder.implementations

import com.github.lunakoly.intellijquicklink.urlbuilder.UrlBuilder
import com.github.lunakoly.intellijquicklink.utils.removeExtension
import com.github.lunakoly.intellijquicklink.utils.removeProtocol

class GithubUrlBuilder : UrlBuilder {
    override fun buildUrl(
        remoteLink: String,
        branch: String,
        filePath: String,
        lineNumber: Int,
    ): String {
        val importantPart = remoteLink
            .removeProtocol()
            .removeExtension()

        return buildString {
            append("https://")
            append(importantPart)
            append("/blob/")
            append(branch)
            append('/')
            append(filePath)
            append("#L")
            append(lineNumber)
        }
    }
}
