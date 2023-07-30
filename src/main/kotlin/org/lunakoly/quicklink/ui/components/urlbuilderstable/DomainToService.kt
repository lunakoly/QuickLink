package org.lunakoly.quicklink.ui.components.urlbuilderstable

import org.lunakoly.quicklink.ui.buildUrlBuildersComboBox
import org.lunakoly.quicklink.urlbuilder.UrlBuilders
import com.intellij.openapi.ui.ComboBox
import com.jetbrains.rd.swing.textProperty
import com.jetbrains.rd.util.reactive.adviseEternal
import javax.swing.JTextField

class DomainToService(
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

        domainField.textProperty().adviseEternal {
            onDomainChanged()
        }
    }
}
