package org.lunakoly.quicklink.ui.components.urlbuilderstable

import org.lunakoly.quicklink.urlbuilder.UrlBuilders
import org.lunakoly.quicklink.utils.ensureCapacity
import org.lunakoly.quicklink.utils.toDomain
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

class ColumnTypeException(message: String) : Exception(message)

@Suppress("TooManyFunctions")
class DomainToServiceTableModel : TableModel {
    private var listDomains = mutableListOf<DomainToService>()

    fun loadDomainsMapping(domainsMapping: Map<String, String>) {
        listDomains = domainsMapping.toDomainsList()
        onSomeDomainChanged()
    }

    private fun onSomeDomainChanged() {
        onDomainChanged?.let {
            val duplicateDomain = getDuplicateDomain()
            it(duplicateDomain)
        }
    }

    fun getDomainsMapping(): Map<String, String> {
        return listDomains.associate { it.domainField.text to it.serviceComboBox.item.name }
    }

    private fun Map<String, String>.toDomainsList(): MutableList<DomainToService> {
        return this
            .map { DomainToService(::onSomeDomainChanged, it.key, it.value) }
            .toMutableList()
    }

    private fun getDuplicateDomain() = listDomains
        .groupingBy { it.domainField.text.toDomain() }
        .eachCount()
        .entries
        .find { it.value > 1 }
        ?.key

    override fun getRowCount() = listDomains.size

    override fun getColumnCount() = 2

    override fun getColumnName(columnIndex: Int) = if (columnIndex == 0) {
        "Domain"
    } else {
        "Service"
    }

    override fun getColumnClass(columnIndex: Int) = if (columnIndex == 0) {
        String::class.java
    } else {
        UrlBuilders::class.java
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int) = true

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any = if (columnIndex == 0) {
        listDomains[rowIndex].domainField.text
    } else {
        listDomains[rowIndex].serviceComboBox.selectedItem
    }

    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
        if (columnIndex == 0) {
            if (value !is String) {
                throw ColumnTypeException("First column may only store a String domain")
            }

            listDomains.ensureCapacity(rowIndex, DomainToService(::onSomeDomainChanged))
            listDomains[rowIndex].domainField.text = value
        } else {
            if (value !is UrlBuilders) {
                throw ColumnTypeException("Second column may only store an UrlBuilders enum value")
            }

            listDomains.ensureCapacity(rowIndex, DomainToService(::onSomeDomainChanged))
            listDomains[rowIndex].serviceComboBox.selectedIndex = value.ordinal
        }
    }

    fun addEmptyRow() {
        listDomains.add(DomainToService(::onSomeDomainChanged))
        onSomeDomainChanged()
    }

    fun deleteRow(rowIndex: Int) {
        listDomains.removeAt(rowIndex)
        onSomeDomainChanged()
    }

    private var onDomainChanged: ((String?) -> Unit)? = null

    fun setDuplicateDomainChangeListener(domainChanged: (String?) -> Unit) {
        onDomainChanged = domainChanged
    }

    val set = mutableSetOf<TableModelListener>()

    override fun addTableModelListener(p0: TableModelListener?) {
        if (p0 != null) this.set.add(p0)
    }

    override fun removeTableModelListener(p0: TableModelListener?) {
        if (p0 != null) this.set.remove(p0)
    }
}
