package com.github.lunakoly.quicklink.utils.ui

import com.github.lunakoly.quicklink.settings.helpers.UrlBuildersComboBoxRenderer
import com.github.lunakoly.quicklink.urlbuilder.UrlBuilders
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.components.BorderLayoutPanel
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.table.TableModel
import kotlin.reflect.KProperty

class SingleAssign<T> {
    class Uninitialized : Exception()
    class Reinitialized : Exception()

    var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw Uninitialized()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        if (value != null) {
            throw Reinitialized()
        }
        value = newValue
    }
}

fun buildPanel(setup: FormBuilder.() -> Unit = {}): JPanel {
    return FormBuilder.createFormBuilder().apply(setup).panel
}

fun <T : JComponent> FormBuilder.build(component: T, setup: T.() -> Unit = {}): T {
    return component.apply(setup).apply {
        addComponent(this)
    }
}

fun <T : JComponent> FormBuilder.buildLabeled(
    label: String,
    topInset: Int,
    labelOnTop: Boolean,
    component: T,
    setup: T.() -> Unit = {}
): T {
    return component.apply(setup).apply {
        addLabeledComponent(JBLabel(label), component, topInset, labelOnTop)
    }
}

fun <T : JComponent> FormBuilder.buildFilledVertically(
    topInset: Int,
    component: T,
    setup: T.() -> Unit = {}
): T {
    return component.apply(setup).apply {
        addComponentFillVertically(component, topInset)
    }
}

fun FormBuilder.buildButton(title: String, setup: JButton.() -> Unit = {}): JButton {
    return JButton(title).apply(setup).apply {
        addComponent(this)
    }
}

fun <T> FormBuilder.buildJBList(setup: JBList<T>.() -> Unit = {}): JBList<T> {
    return JBList<T>().apply(setup).apply {
        addComponent(this)
    }
}

fun FormBuilder.buildJBTable(model: TableModel, setup: JBTable.() -> Unit = {}): JBTable {
    return JBTable(model).apply(setup).apply {
        addComponent(this)
    }
}

//fun buildToolbarDecorator(table: JTable, setup: ToolbarDecorator.() -> Unit = {}): ToolbarDecorator {
//    return ToolbarDecorator.createDecorator(table).apply(setup)
//}

fun FormBuilder.buildToolbarDecorator(
    table: JTable,
    setup: ToolbarDecorator.() -> Unit = {}
): ToolbarDecorator {
    return ToolbarDecorator.createDecorator(table).apply(setup).also {
        addComponentFillVertically(it.createPanel(), 0)
    }
}

fun FormBuilder.buildLabel(text: String, setup: JBLabel.() -> Unit = {}): JBLabel {
    return JBLabel(text).apply(setup).apply {
        addComponent(this)
    }
}

fun FormBuilder.buildBorderLayoutPanel(setup: BorderLayoutPanel.() -> Unit = {}): BorderLayoutPanel {
    return BorderLayoutPanel().apply(setup).apply {
        addComponentFillVertically(this, 0)
    }
}

fun buildUrlBuildersComboBox(
    initial: UrlBuilders = UrlBuilders.GITHUB_BUILDER
): ComboBox<UrlBuilders> {
    return ComboBox(UrlBuilders.values()).apply {
        selectedIndex = initial.ordinal
        renderer = UrlBuildersComboBoxRenderer()
        isEnabled = true
        isEditable = false
    }
}
