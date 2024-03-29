package org.lunakoly.quicklink.ui.components.urlbuilderstable

import org.lunakoly.quicklink.ui.buildUrlBuildersComboBox
import com.intellij.ui.table.JBTable
import javax.swing.DefaultCellEditor

fun buildDomainToService(setup: JBTable.(DomainToServiceTableModel) -> Unit): JBTable {
    val tableModel = DomainToServiceTableModel()
    return JBTable(tableModel).apply {
        getColumn("Service").cellEditor = DefaultCellEditor(buildUrlBuildersComboBox())
        getColumn("Service").cellRenderer = ServiceTableRenderer()
        setup(tableModel)
    }
}
