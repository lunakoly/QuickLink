package com.github.lunakoly.intellijquicklink.repository

class RepositoryInfo(
    val branch: String,
    val remotes: Map<String, String>,
) {
    val remotesNames get() = remotes.keys.toList()
}
