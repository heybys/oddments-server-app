package com.heybys.oddments.base.domain

import java.util.Arrays

abstract class ValueObject<T : ValueObject<T>> {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other.javaClass != javaClass) return false

        @Suppress("UNCHECKED_CAST")
        return equals(other as T)
    }

    open fun equals(other: T?): Boolean {
        if (other == null) return false
        return Arrays.equals(equalityFields, other.equalityFields)
    }

    override fun hashCode(): Int {
        var hash = 17
        for (field in equalityFields) {
            hash = hash * 31 + (field?.hashCode() ?: 0)
        }
        return hash
    }

    @Suppress("UNCHECKED_CAST")
    protected open val equalityFields: Array<Any?>
        get() =
            javaClass.declaredFields
                .map { field ->
                    field.isAccessible = true
                    try {
                        field.get(this)
                    } catch (e: IllegalAccessException) {
                        throw RuntimeException(e)
                    }
                }
                .toTypedArray()
}
