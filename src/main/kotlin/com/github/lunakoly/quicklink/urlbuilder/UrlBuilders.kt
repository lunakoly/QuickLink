package com.github.lunakoly.quicklink.urlbuilder

import com.github.lunakoly.quicklink.settings.QuickLinkSettingsState
import com.github.lunakoly.quicklink.urlbuilder.implementations.GithubUrlBuilder
import com.github.lunakoly.quicklink.urlbuilder.implementations.GitlabUrlBuilder
import com.github.lunakoly.quicklink.utils.PopupException
import com.github.lunakoly.quicklink.settings.QuickLinkSettingsStateimport

com.github.lunakoly.quicklink.utils.toDomain

class UnsupportedUrlFormatException(domain: String) : PopupException(
    "Only Github and Gitlab URL's are supported. The selected remote link domain is: '$domain'.",
    "Non-Github or Non-Gitlab URL"
)

private val namesMapping by lazy {
    UrlBuilders.values().associateBy {
        it.name
    }
}

private val defaultDomainsMapping = mapOf(
    "github.com" to UrlBuilders.GITHUB_BUILDER,
    "gitlab.com" to UrlBuilders.GITLAB_BUILDER,
)

enum class UrlBuilders(
    val serviceName: String,
    private val backend: UrlBuilder,
) : UrlBuilder by backend {
    GITHUB_BUILDER("GitHub", GithubUrlBuilder()),
    GITLAB_BUILDER("GitLab", GitlabUrlBuilder());

    companion object {
        fun fromName(name: String) = namesMapping[name] ?: GITHUB_BUILDER

        fun fromDomain(domain: String): UrlBuilders {
            QuickLinkSettingsState.instance.rawDomainsMapping[domain]?.let {
                return fromName(it)
            }

            return defaultDomainsMapping[domain] ?: throw UnsupportedUrlFormatException(domain)
        }
    }
}
