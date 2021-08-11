package com.github.lunakoly.quicklink.ui.components.urlbuilderstable

import com.github.lunakoly.quicklink.urlbuilder.UrlBuilders
import com.intellij.openapi.ui.ComboBox
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.TableCellRenderer

class ServiceTableRenderer : ComboBox<String>(), TableCellRenderer {

    override fun getTableCellRendererComponent(
        table: JTable,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        return this.apply {
            removeAllItems()

            background = if (isSelected) {
                table.selectionBackground
            } else {
                table.selectionForeground
            }

            (value as? UrlBuilders)?.let {
                addItem(it.serviceName)
            }
        }
    }
}
