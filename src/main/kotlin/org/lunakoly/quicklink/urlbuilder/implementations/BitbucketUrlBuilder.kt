package org.lunakoly.quicklink.urlbuilder.implementations

import org.lunakoly.quicklink.repository.RepositoryInfo
import org.lunakoly.quicklink.urlbuilder.Selection
import org.lunakoly.quicklink.urlbuilder.UrlBuilder
import org.lunakoly.quicklink.utils.cleanSSHUrl
import org.lunakoly.quicklink.utils.removeProtocol
import org.lunakoly.quicklink.utils.removeTrailingSlash
import org.lunakoly.quicklink.utils.removeUrlExtension
import org.lunakoly.quicklink.utils.removeUrlParameters

class BitbucketUrlBuilder : UrlBuilder {
    override fun buildUrl(
        remoteLink: String,
        repositoryInfo: RepositoryInfo,
        filePath: String,
        selection: Selection,
    ): String {
        val importantPart = remoteLink
            .removeProtocol()
            .cleanSSHUrl()
            .removeUrlParameters()
            .removeTrailingSlash()
            .removeUrlExtension()

        return buildString {
            append("https://")
            append(importantPart)
            append("/src/")
            // We can't use the branch here, because the
            // current user might have not updated their
            // local branch, so the link would point
            // to the code that might have changed.
            append(repositoryInfo.commitHash)
            append('/')
            append(filePath)
            append("#lines-")
            when (selection) {
                is Selection.MultilineSelection -> {
                    // Bitbucket currently doesn't support column selection
                    append(selection.start.lineNumber)
                    append(":")
                    append(selection.end.lineNumber)
                }
                is Selection.SingleLinkSelection -> {
                    append(selection.lineOffset.lineNumber)
                }
            }
        }
    }
}
