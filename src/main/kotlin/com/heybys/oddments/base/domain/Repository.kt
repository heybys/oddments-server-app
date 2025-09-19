package com.heybys.oddments.base.domain

interface Repository<D : AggregateRoot<D, I>, I> {
    fun save(root: D): D

    fun find(id: I): D

    fun remove(id: I)

    fun remove(root: D)
}
