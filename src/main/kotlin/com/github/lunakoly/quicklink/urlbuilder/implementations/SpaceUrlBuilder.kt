package com.github.lunakoly.quicklink.urlbuilder.implementations

import com.github.lunakoly.quicklink.repository.RepositoryInfo
import com.github.lunakoly.quicklink.urlbuilder.UrlBuilder
import com.github.lunakoly.quicklink.utils.cleanSSHUrl
import com.github.lunakoly.quicklink.utils.removeProtocol
import com.github.lunakoly.quicklink.utils.removeTrailingSlash
import com.github.lunakoly.quicklink.utils.removeUrlExtension
import com.github.lunakoly.quicklink.utils.removeUrlParameters

class SpaceUrlBuilder : UrlBuilder {
    override fun buildUrl(
        remoteLink: String,
        repositoryInfo: RepositoryInfo,
        filePath: String,
        lineNumber: Int,
    ): String {
        val importantPart = remoteLink
            .removeProtocol()
            .cleanSSHUrl()
            .removeUrlParameters()
            .removeTrailingSlash()
            .removeUrlExtension()
            .replaceFirst("""([^/]*)/([^/]*)/(.*)""".toRegex(), "$1/p/$2/repositories/$3/files/")

        return buildString {
            append(importantPart)
            // We can't use the branch here, because the
            // current user might have not updated their
            // local branch, so the link would point
            // to the code that might have changed.
            append(repositoryInfo.commitHash)
            append('/')
            append(filePath)
            append("?tab=source&line=")
            append(lineNumber)
            append("&lines-count=1")
        }
    }
}
