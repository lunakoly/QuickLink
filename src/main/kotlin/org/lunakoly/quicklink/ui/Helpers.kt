package org.lunakoly.quicklink.ui

import org.lunakoly.quicklink.ui.components.PopupExceptionsList
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint

fun Editor.showClickableListOf(
    things: List<String>,
    title: String,
    onClick: (String) -> Unit
) {
    val list = PopupExceptionsList(things, title, onClick)
    JBPopupFactory
        .getInstance()
        .createListPopup(list)
        .showInBestPositionFor(this)
}

fun Editor.showClickableListIfNeeded(
    things: List<String>,
    title: String,
    onClick: (String) -> Unit
) {
    if (things.size == 1) {
        onClick(things.first())
    } else {
        showClickableListOf(things, title, onClick)
    }
}

const val FADE_OUT_DELAY_MS = 3000L

fun Project.toast(message: String) {
    val statusBar = WindowManager.getInstance().getStatusBar(this)
    val component = statusBar.component ?: run {
        println("statusBar component is null")
        return
    }

    JBPopupFactory.getInstance()
        .createHtmlTextBalloonBuilder(message, MessageType.INFO, null)
        .setFadeoutTime(FADE_OUT_DELAY_MS)
        .createBalloon()
        .show(
            RelativePoint.getCenterOf(component),
            Balloon.Position.atRight
        )
}
