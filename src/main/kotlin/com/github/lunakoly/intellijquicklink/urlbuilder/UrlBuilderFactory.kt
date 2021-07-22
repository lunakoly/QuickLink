package com.github.lunakoly.intellijquicklink.urlbuilder

import com.github.lunakoly.intellijquicklink.urlbuilder.implementations.GithubUrlBuilder
import com.github.lunakoly.intellijquicklink.utils.PopupException
import com.github.lunakoly.intellijquicklink.utils.domainStartsWith

class UnsupportedUrlFormatException(message: String, title: String) : PopupException(message, title)

object UrlBuilderFactory {
    fun guessByLink(link: String): UrlBuilder = when {
        link.domainStartsWith("github.com") -> {
            GithubUrlBuilder()
        }
        else -> {
            throw UnsupportedUrlFormatException(
                "Only Github URL's are supported",
                "Non-Github UR"
            )
        }
    }
}
