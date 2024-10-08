package org.lunakoly.quicklink.repository

import com.intellij.openapi.vfs.VirtualFile

class RepositoryInfo(
    val root: VirtualFile,
    @Suppress("unused")
    val branch: String?,
    val commitHash: String,
)
