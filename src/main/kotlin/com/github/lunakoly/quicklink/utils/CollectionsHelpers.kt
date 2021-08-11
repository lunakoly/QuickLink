package com.github.lunakoly.quicklink.utils

fun <T, K> Map<T, K>.isSameAs(other: Map<T, K>): Boolean {
    if (size != other.size) {
        return false
    }
    return other.all { this[it.key] == it.value }
}

fun <T> MutableList<T>.ensureCapacity(requiredIndex: Int, filler: T) {
    for (it in size..requiredIndex) {
        add(filler)
    }
}
