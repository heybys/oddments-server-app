package com.heybys.oddments.base.domain

abstract class DomainEntity<T : DomainEntity<T, I>, I> : BaseEntity() {

    abstract fun getId(): I?

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other.javaClass != javaClass) return false

        @Suppress("UNCHECKED_CAST")
        return equals(other as T)
    }

    open fun equals(other: T?): Boolean {
        if (other == null) return false
        val id = getId()
        if (id == null) return false

        return if (other.javaClass == javaClass) {
            id == other.getId()
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return getId()?.hashCode() ?: 0
    }
}
