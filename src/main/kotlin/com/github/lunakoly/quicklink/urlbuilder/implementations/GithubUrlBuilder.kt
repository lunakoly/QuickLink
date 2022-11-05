package com.github.lunakoly.quicklink.urlbuilder.implementations

import com.github.lunakoly.quicklink.repository.RepositoryInfo
import com.github.lunakoly.quicklink.urlbuilder.UrlBuilder
import com.github.lunakoly.quicklink.utils.cleanSSHUrl
import com.github.lunakoly.quicklink.utils.removeProtocol
import com.github.lunakoly.quicklink.utils.removeTrailingSlash
import com.github.lunakoly.quicklink.utils.removeUrlExtension
import com.github.lunakoly.quicklink.utils.removeUrlParameters

class GithubUrlBuilder : UrlBuilder {
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

        return buildString {
            append(importantPart)
            append("/blob/")
            // We can't use the branch here, because the
            // current user might have not updated their
            // local branch, so the link would point
            // to the code that might have changed.
            append(repositoryInfo.commitHash)
            append('/')
            append(filePath)
            append("#L")
            append(lineNumber)
        }
    }
}
