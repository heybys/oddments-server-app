package com.heybys.oddments.base.domain

abstract class LongTypeIdentifier(private val id: Long) : ValueObject<LongTypeIdentifier>() {

    fun longValue(): Long = id

    fun nextValue(): Long = id + 1

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override val equalityFields: Array<Any?>
        get() = arrayOf(id)

    override fun toString(): String {
        return "${this.javaClass.simpleName} : $id"
    }
}
