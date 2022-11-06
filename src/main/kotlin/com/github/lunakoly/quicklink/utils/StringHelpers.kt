package com.github.lunakoly.quicklink.utils

const val HTTPS_PROTOCOL_SEPARATOR = "://"
const val GIT_PROTOCOL_SEPARATOR = "@"
const val SSH_URL_IDENTIFIER = "git@"

fun String.removeProtocol(): String {
    val protocolSeparator = when {
        startsWith(SSH_URL_IDENTIFIER) -> GIT_PROTOCOL_SEPARATOR
        else -> HTTPS_PROTOCOL_SEPARATOR
    }

    val index = this.indexOf(protocolSeparator)

    return if (index != -1) {
        substring(index + protocolSeparator.length)
    } else {
        return this
    }
}

fun String.takeBefore(symbol: Char): String {
    val index = this.indexOf(symbol)

    return if (index != -1) {
        this.substring(0, index)
    } else {
        this
    }
}

fun String.removeUrlParameters() = takeBefore('&')

fun String.removeTrailingSlash(): String {
    return if (this.endsWith('/')) {
        this.substring(0, this.length - 1)
    } else {
        this
    }
}

fun String.beforeFirstSlash() = takeBefore('/')

fun String.removeUrlExtension(): String {
    val slashIndex = this.indexOfLast { it == '/' }
    val dotIndex = this.indexOfLast { it == '.' }

    if (dotIndex != -1 && slashIndex != -1 && slashIndex < dotIndex) {
        return this.substring(0, dotIndex)
    }

    return this
}

fun String.cleanSSHUrl(): String {
    // replaces the : with / in
    // git@github.com:lunakoly/QuickLink.git
    return this.replaceFirst(":", "/")
}

const val FOLDER_STEP_UP = "../"

fun String.removeDirectoryStepUp(): String {
    return if (FOLDER_STEP_UP in this) {
        this.substring(FOLDER_STEP_UP.length)
    } else {
        this
    }
}

fun String.domainStartsWith(prefix: String): Boolean {
    return removeProtocol().startsWith(prefix)
}

fun String.toDomain() = this
    .removeProtocol()
    .cleanSSHUrl()
    .removeUrlParameters()
    .beforeFirstSlash()
