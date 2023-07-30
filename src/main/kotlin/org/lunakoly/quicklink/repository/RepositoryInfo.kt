package org.lunakoly.quicklink.repository

import com.intellij.openapi.vfs.VirtualFile

class RepositoryInfo(
    val root: VirtualFile,
    val branch: String?,
    val commitHash: String,
    val remotes: Map<String, String>,
) {
    val remotesNames get() = remotes.keys.toList()
}
