package com.github.lunakoly.quicklink.urlbuilder

import com.github.lunakoly.quicklink.urlbuilder.implementations.GithubUrlBuilder
import com.github.lunakoly.quicklink.utils.PopupException
import com.github.lunakoly.quicklink.utils.domainStartsWith

class UnsupportedUrlFormatException(message: String, title: String) : PopupException(message, title)

object UrlBuilderFactory {
    fun guessByLink(link: String): UrlBuilder = when {
        link.domainStartsWith("github.com") -> {
            GithubUrlBuilder()
        }
        else -> {
            throw UnsupportedUrlFormatException(
                "Only Github URL's are supported. The selected remote link is: '$link'.",
                "Non-Github URL"
            )
        }
    }
}
