package com.github.lunakoly.quicklink.settings.helpers

import com.github.lunakoly.quicklink.urlbuilder.UrlBuilders
import com.intellij.openapi.ui.ComboBox
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.TableCellRenderer

class UrlBuildersTableRenderer : ComboBox<String>(), TableCellRenderer {
    init {
//        insets.apply {
//            top = 0
//            left = 0
//            right = 0
//            bottom = 0
//        }
//        border = EmptyBorder(0, 0, 0, 0)
    }

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
//            isFocusable = false

            background = if (isSelected) {
                table.selectionBackground
            } else {
                table.selectionForeground
            }

//            if (isSelected) {
//                background = table.selectionBackground
//                foreground = table.selectionForeground
//            } else {
//                background = table.background
//                foreground = table.background
//            }

            (value as? UrlBuilders)?.let {
                addItem(it.serviceName)
            }
        }
    }
}