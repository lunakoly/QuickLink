package com.github.lunakoly.quicklink.repository

class RepositoryInfo(
    val branch: String?,
    val commitHash: String,
    val remotes: Map<String, String>,
) {
    val remotesNames get() = remotes.keys.toList()
}
