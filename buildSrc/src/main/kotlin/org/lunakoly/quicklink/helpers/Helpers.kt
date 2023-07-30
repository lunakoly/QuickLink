@file:Suppress("unused")

package org.lunakoly.quicklink.helpers

fun <T> pickFirstMiddleAndLast(elements: List<T>) = when {
    elements.isEmpty() -> emptySet()
    else -> setOf(elements.first(), elements.get(elements.lastIndex / 2), elements.last())
}
