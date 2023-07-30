package org.lunakoly.quicklink.ui.components

import org.lunakoly.quicklink.utils.catchingPopupExceptions
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep

class PopupExceptionsList(
    options: List<String>,
    title: String,
    private val onSelected: (String) -> Unit,
) : BaseListPopupStep<String>(title, options) {
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
                finalValue?.let(onSelected)
            }
        }
    }
}
