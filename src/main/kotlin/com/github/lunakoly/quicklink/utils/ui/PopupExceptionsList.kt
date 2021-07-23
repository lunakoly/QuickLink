package com.github.lunakoly.quicklink.utils.ui

import com.github.lunakoly.quicklink.utils.catchingPopupExceptions
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep

class PopupExceptionsList(
    remotes: List<String>,
    private val buildLink: (String) -> Unit,
) : BaseListPopupStep<String>("Select a Remote", remotes) {
    override fun getTextFor(value: String?) = value ?: "<no value>"

    private var finalValue: String? = null

    override fun onChosen(selectedValue: String?, finalChoice: Boolean): PopupStep<*>? {
        if (finalChoice) {
            finalValue = selectedValue
        }

        return super.onChosen(selectedValue, finalChoice)
    }

    override fun getFinalRunnable(): Runnable {
        return Runnable {
            catchingPopupExceptions {
                finalValue?.let(buildLink)
            }
        }
    }
}
