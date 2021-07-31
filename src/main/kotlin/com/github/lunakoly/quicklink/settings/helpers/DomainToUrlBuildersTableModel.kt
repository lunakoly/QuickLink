package com.github.lunakoly.quicklink.settings.helpers

import com.github.lunakoly.quicklink.urlbuilder.UrlBuilders
import com.github.lunakoly.quicklink.utils.ensureCapacity
import com.github.lunakoly.quicklink.utils.toDomain
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

class DomainToUrlBuildersTableModel : TableModel {
    private var listDomains = mutableListOf<DomainToUrlBuilder>()

    var onDuplicateDomainAppear: ((String) -> Unit)? = null

    var onDuplicateDomainDisappear: (() -> Unit)? = null

    fun loadDomainsMapping(domainsMapping: Map<String, String>) {
        listDomains = domainsMapping.toDomainsList()
        onSomeDomainChanged()
    }

    private fun onSomeDomainChanged() {
        val duplicateDomain = getDuplicateDomain()

        if (duplicateDomain != null) {
            onDuplicateDomainAppear?.invoke(duplicateDomain)
        } else {
            onDuplicateDomainDisappear?.invoke()
        }
    }

    fun getDomainsMapping(): Map<String, String> {
        return listDomains.associate { it.domainField.text to it.serviceComboBox.item.name }
    }

    private fun Map<String, String>.toDomainsList(): MutableList<DomainToUrlBuilder> {
        return this
            .map { DomainToUrlBuilder(::onSomeDomainChanged, it.key, it.value) }
            .toMutableList()
    }

    private fun getDuplicateDomain() = listDomains
        .groupingBy { it.domainField.text.toDomain() }
        .eachCount()
        .entries
        .find { it.value > 1 }
        ?.key

    val set = mutableSetOf<TableModelListener>()

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
                throw Exception("First column may only store a String domain")
            }

            listDomains.ensureCapacity(rowIndex, DomainToUrlBuilder(::onSomeDomainChanged))
            listDomains[rowIndex].domainField.text = value
        } else {
            if (value !is UrlBuilders) {
                throw Exception("Second column may only store an UrlBuilders enum value")
            }

            listDomains.ensureCapacity(rowIndex, DomainToUrlBuilder(::onSomeDomainChanged))
            listDomains[rowIndex].serviceComboBox.selectedIndex = value.ordinal
        }
    }

    override fun addTableModelListener(p0: TableModelListener?) {
        if (p0 != null) this.set.add(p0)
    }

    override fun removeTableModelListener(p0: TableModelListener?) {
        if (p0 != null) this.set.remove(p0)
    }

    fun addEmptyRow() {
        listDomains.add(DomainToUrlBuilder(::onSomeDomainChanged))
        onSomeDomainChanged()
    }

    fun deleteRow(rowIndex: Int) {
        listDomains.removeAt(rowIndex)
        onSomeDomainChanged()
    }
}
