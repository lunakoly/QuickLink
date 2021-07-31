package com.github.lunakoly.quicklink.settings.helpers

import com.github.lunakoly.quicklink.urlbuilder.UrlBuilders
import com.github.lunakoly.quicklink.utils.ui.buildUrlBuildersComboBox
import com.intellij.openapi.ui.ComboBox
import com.jetbrains.rd.swing.textProperty
import com.jetbrains.rd.util.reactive.adviseEternal
import javax.swing.JTextField

class DomainToUrlBuilder(
    val onDomainChanged: () -> Unit,
    var domainField: JTextField = JTextField("some.domain"),
    var serviceComboBox: ComboBox<UrlBuilders> = buildUrlBuildersComboBox(UrlBuilders.GITHUB_BUILDER),
) {
    constructor(onAction: () -> Unit, domain: String, service: String) : this(
        onAction,
        JTextField(domain),
        buildUrlBuildersComboBox(UrlBuilders.fromName(service)),
    )

    init {
        domainField.toolTipText = "Enter domain"
//        domainField.text

//        domainField.document.addDocumentListener(object : DocumentListener {
//            override fun changedUpdate(event: DocumentEvent?) {
//                onDomainChanged()
//            }
//
//            override fun insertUpdate(event: DocumentEvent?) {
//                onDomainChanged()
//            }
//
//            override fun removeUpdate(event: DocumentEvent?) {
//                onDomainChanged()
//            }
//        })
//
//        domainField.addPropertyChangeListener {
//            onDomainChanged()
//        }
//
//        domainField.addKeyListener(object : KeyAdapter() {
//            override fun keyTyped(event: KeyEvent?) {
//                onDomainChanged()
//            }
//        })
//
//        domainField.addActionListener {
//            onDomainChanged()
//        }

        domainField.textProperty().adviseEternal {
            onDomainChanged()
        }
    }
}