package org.lunakoly.quicklink.settings

import org.lunakoly.quicklink.ui.buildLabel
import org.lunakoly.quicklink.ui.buildPanel
import org.lunakoly.quicklink.ui.buildToolbarDecorator
import org.lunakoly.quicklink.ui.components.urlbuilderstable.DomainToServiceTableModel
import org.lunakoly.quicklink.ui.components.urlbuilderstable.buildDomainToService
import org.lunakoly.quicklink.utils.SingleAssign
import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.table.JBTable
import java.awt.Color
import javax.swing.table.JTableHeader

private const val HELP_TEXT =
    "Use the following table to associate a custom corporate domain with the corresponding service"

private fun getErrorText(duplicateDomain: String): String {
    return "There are duplicate domains in the table: '$duplicateDomain'"
}

class QuickLinkSettingsComponent {
    var domainsMapping: Map<String, String>
        get() = tableModel.getDomainsMapping()
        set(value) {
            tableModel.loadDomainsMapping(value).also {
                tableDomains.updateUI()
            }
        }

    private var errorLabel by SingleAssign<JBLabel>()
    private var tableDomains by SingleAssign<JBTable>()
    private var toolbarDecorator by SingleAssign<ToolbarDecorator>()
    private var tableModel by SingleAssign<DomainToServiceTableModel>()

    val jPanel = buildPanel {
        buildLabel(HELP_TEXT)
        buildPanel()
        errorLabel = buildLabel("") {
            isVisible = false
            foreground = Color.RED
        }

        tableDomains = buildDomainToService { model ->
            tableModel = model.apply {
                setDuplicateDomainChangeListener {
                    onDuplicateDomainChange(it)
                }
            }
            tableHeader = JTableHeader(columnModel)
            isVisible = true
            fillsViewportHeight = true
        }

        toolbarDecorator = buildToolbarDecorator(tableDomains) {
            this.setToolbarPosition(ActionToolbarPosition.BOTTOM)

            setAddAction {
                onApply()
            }

            setRemoveAction {
                onDelete(tableDomains.selectedRow)
            }
        }
    }

    private fun onApply() {
        (tableDomains.model as? DomainToServiceTableModel)?.addEmptyRow()
        tableDomains.updateUI()
    }

    private fun onDelete(rowIndex: Int?) {
        if (rowIndex != null) {
            (tableDomains.model as? DomainToServiceTableModel)?.deleteRow(rowIndex)
        }
        tableDomains.updateUI()
    }

    private fun onDuplicateDomainChange(duplicateDomain: String?) {
        if (duplicateDomain != null) {
            errorLabel.isVisible = true
            errorLabel.text = getErrorText(duplicateDomain)
        } else {
            errorLabel.isVisible = false
        }
        updateAllUI()
    }

    private fun updateAllUI() {
        jPanel.updateUI()
    }
}
