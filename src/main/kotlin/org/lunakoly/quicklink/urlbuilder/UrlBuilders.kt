package org.lunakoly.quicklink.urlbuilder

import org.lunakoly.quicklink.settings.QuickLinkSettingsState
import org.lunakoly.quicklink.urlbuilder.implementations.GithubUrlBuilder
import org.lunakoly.quicklink.urlbuilder.implementations.GitlabUrlBuilder
import org.lunakoly.quicklink.urlbuilder.implementations.SpaceUrlBuilder
import org.lunakoly.quicklink.urlbuilder.implementations.BitbucketUrlBuilder
import org.lunakoly.quicklink.utils.PopupException

class UnsupportedUrlFormatException(domain: String) : PopupException(
    "Only ${UrlBuilders.nameVariants} URLs are supported. The selected remote link domain is: '$domain'.",
    "Non-${UrlBuilders.nameVariants} URL"
)

private val namesMapping by lazy {
    UrlBuilders.values().associateBy {
        it.name
    }
}

private val defaultDomainsMapping = mapOf(
    "github.com" to UrlBuilders.GITHUB_BUILDER,
    "gitlab.com" to UrlBuilders.GITLAB_BUILDER,
    "bitbucket.org" to UrlBuilders.BITBUCKET_BUILDER,
)

enum class UrlBuilders(
    val serviceName: String,
    private val backend: UrlBuilder,
) : UrlBuilder by backend {
    GITHUB_BUILDER("GitHub", GithubUrlBuilder()),
    GITLAB_BUILDER("GitLab", GitlabUrlBuilder()),
    SPACE_BUILDER("Space", SpaceUrlBuilder()),
    BITBUCKET_BUILDER("Bitbucket", BitbucketUrlBuilder());

    companion object {
        fun fromName(name: String) = namesMapping[name] ?: GITHUB_BUILDER

        fun fromDomain(domain: String): UrlBuilders {
            QuickLinkSettingsState.instance.rawDomainsMapping[domain]?.let {
                return fromName(it)
            }

            return defaultDomainsMapping[domain] ?: throw UnsupportedUrlFormatException(domain)
        }

        val nameVariants: String get() = values().joinToString("/") { it.name }
    }
}
