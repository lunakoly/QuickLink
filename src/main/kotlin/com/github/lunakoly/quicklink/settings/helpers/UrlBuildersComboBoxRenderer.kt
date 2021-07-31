package com.github.lunakoly.quicklink.settings.helpers

import com.github.lunakoly.quicklink.urlbuilder.UrlBuilders
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

class UrlBuildersComboBoxRenderer : DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
            .apply {
                (value as? UrlBuilders)?.let {
                    text = it.serviceName
                }
            }
    }
}
