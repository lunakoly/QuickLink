package com.github.lunakoly.intellijquicklink.utils

const val PROTOCOL_SEPARATOR = "://"

fun String.removeProtocol(): String {
    val index = this.indexOf(PROTOCOL_SEPARATOR)

    if (index != -1) {
        return this.substring(index + PROTOCOL_SEPARATOR.length)
    }

    return this
}

fun String.removeExtension(): String {
    val index = this.indexOfLast { it == '.' }

    if (index != -1) {
        return this.substring(0, index)
    }

    return this
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
