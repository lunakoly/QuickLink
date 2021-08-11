package com.github.lunakoly.quicklink.settings

import com.github.lunakoly.quicklink.utils.isSameAs
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

// A default constructor with no arguments is required because this implementation
// is registered as an applicationConfigurable EP

class QuickLinkSettingsConfigurable : Configurable {
    private var component: QuickLinkSettingsComponent? = null

    override fun getDisplayName() = "Quick Link"

    override fun createComponent(): JComponent {
        val component = QuickLinkSettingsComponent()
        this.component = component
        return component.jPanel
    }

    override fun isModified(): Boolean {
        val state = QuickLinkSettingsState.instance
        return component?.domainsMapping?.isSameAs(state.domainsMapping) == false
    }

    override fun apply() {
        val state = QuickLinkSettingsState.instance

        component?.domainsMapping?.let {
            state.domainsMapping = it.toMap()
        }
    }

    override fun reset() {
        val state = QuickLinkSettingsState.instance

        component?.let {
            it.domainsMapping = state.domainsMapping.toMutableMap()
        }
    }

    override fun disposeUIResources() {
        component = null
    }
}
