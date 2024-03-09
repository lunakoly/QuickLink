package org.lunakoly.quicklink.urlbuilder.implementations

import org.lunakoly.quicklink.repository.RepositoryInfo
import org.lunakoly.quicklink.urlbuilder.Selection
import org.lunakoly.quicklink.urlbuilder.UrlBuilder
import org.lunakoly.quicklink.utils.cleanSSHUrl
import org.lunakoly.quicklink.utils.removeProtocol
import org.lunakoly.quicklink.utils.removeTrailingSlash
import org.lunakoly.quicklink.utils.removeUrlExtension
import org.lunakoly.quicklink.utils.removeUrlParameters

class SpaceUrlBuilder : UrlBuilder {
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
            .replaceFirst("""([^/]*)/([^/]*)/(.*)""".toRegex(), "$1/p/$2/repositories/$3/files/")

        return buildString {
            append("https://")
            append(importantPart)
            // We can't use the branch here, because the
            // current user might have not updated their
            // local branch, so the link would point
            // to the code that might have changed.
            append(repositoryInfo.commitHash)
            append('/')
            append(filePath)
            append("?tab=source&line=")
            when (selection) {
                is Selection.MultilineSelection -> {
                    append(selection.start.lineNumber)
                    append("&lines-count=")
                    append(selection.end.lineNumber - selection.start.lineNumber)
                }
                is Selection.SingleLinkSelection -> {
                    append(selection.lineOffset.lineNumber)
                    append("&lines-count=")
                    append("1")
                }
            }
        }
    }
}
