package org.lunakoly.quicklink.utils

import com.intellij.openapi.ui.Messages

open class PopupException(override val message: String, val title: String) : Exception(message)

private fun reportInfo(message: String, title: String) {
    Messages.showMessageDialog(message, title, Messages.getInformationIcon())
}

fun catchingPopupExceptions(doSomething: () -> Unit) {
    try {
        doSomething()
    } catch (exception: PopupException) {
        reportInfo(exception.message, exception.title)
    }
}
