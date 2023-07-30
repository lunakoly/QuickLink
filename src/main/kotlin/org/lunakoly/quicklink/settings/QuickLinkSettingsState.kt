package org.lunakoly.quicklink.settings

import org.lunakoly.quicklink.utils.toDomain
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "org.lunakoly.quicklink.settings.QuickLinkSettingsState",
    storages = [Storage("QuickLinkSettings.xml")]
)
class QuickLinkSettingsState : PersistentStateComponent<QuickLinkSettingsState> {
    companion object {
        val instance: QuickLinkSettingsState
            get() = ApplicationManager.getApplication()
                .getService(QuickLinkSettingsState::class.java)
    }

    var domainsMapping = mapOf<String, String>()

    val rawDomainsMapping get() = domainsMapping.mapKeys { it.key.toDomain() }

    override fun getState() = this

    override fun loadState(state: QuickLinkSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
