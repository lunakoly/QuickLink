package org.lunakoly.quicklink.utils

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
