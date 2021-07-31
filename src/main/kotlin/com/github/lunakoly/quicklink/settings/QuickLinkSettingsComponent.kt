package com.github.lunakoly.quicklink.settings

import com.github.lunakoly.quicklink.settings.helpers.DomainToUrlBuildersTableModel
import com.github.lunakoly.quicklink.settings.helpers.UrlBuildersTableRenderer
import com.github.lunakoly.quicklink.utils.ui.*
import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.table.JBTable
import com.intellij.util.castSafelyTo
import com.intellij.util.ui.UIUtil
import java.awt.Color
import java.awt.event.*
import javax.swing.DefaultCellEditor
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import com.github.lunakoly.quicklink.settings.helpers.DomainToUrlBuildersTableModelimport

javax.swing.table.JTableHeader

private const val HELP_TEXT = "Add custom domain mapping here"

private const val RED_COLOR = "#D32F2F"

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

    private val tableModel = DomainToUrlBuildersTableModel().apply {
        onDuplicateDomainAppear = {
            errorLabel.isVisible = true
            errorLabel.text = getErrorText(it)
            updateAllUI()
        }

        onDuplicateDomainDisappear = {
            errorLabel.isVisible = false
            updateAllUI()
        }
    }

    val jPanel = buildPanel {
        buildLabel(HELP_TEXT)

        errorLabel = buildLabel("") {
            isVisible = false
//            foreground = Color.getColor(RED_COLOR)
            foreground = Color.RED
        }

        tableDomains = JBTable(tableModel).apply {
            tableHeader = JTableHeader(columnModel)
            isVisible = true
            fillsViewportHeight = true
            getColumn("Service").cellEditor = DefaultCellEditor(buildUrlBuildersComboBox())
            getColumn("Service").cellRenderer = UrlBuildersTableRenderer()

//            addPropertyChangeListener {
//                println("!!!!")
//            }

//            addKeyListener(object : KeyAdapter() {
//                override fun keyTyped(e: KeyEvent?) {
//                    println("???")
//                }
//            })
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
        tableDomains.model.castSafelyTo<DomainToUrlBuildersTableModel>()?.addEmptyRow()
        tableDomains.updateUI()
    }

    private fun onDelete(rowIndex: Int?) {
        if (rowIndex != null) {
            tableDomains.model.castSafelyTo<DomainToUrlBuildersTableModel>()?.deleteRow(rowIndex)
        }
        tableDomains.updateUI()
    }

    private fun updateAllUI() {
        jPanel.updateUI()
    }
}
