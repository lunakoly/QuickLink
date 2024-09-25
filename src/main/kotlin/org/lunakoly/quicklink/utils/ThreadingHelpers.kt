package org.lunakoly.quicklink.utils

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ThrowableComputable

fun Project?.runInBackground(progressTitle: String, block: () -> Unit) {
    ProgressManager.getInstance().runProcessWithProgressSynchronously(
        ThrowableComputable(block), progressTitle, true, this,
    )
}
