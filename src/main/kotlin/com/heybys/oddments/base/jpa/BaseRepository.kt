package com.heybys.oddments.base.jpa

import com.heybys.oddments.base.domain.AggregateRoot
import com.heybys.oddments.base.domain.Repository
import org.springframework.data.jpa.repository.JpaRepository

abstract class BaseRepository<D : AggregateRoot<D, I>, I : Any, R : JpaRepository<D, I>>(
    protected val repository: R,
) : Repository<D, I> {

    override fun save(root: D): D = repository.save(root)

    override fun find(id: I): D = repository.findById(id).orElse(null)

    override fun remove(id: I) {
        repository.deleteById(id)
    }

    override fun remove(root: D) {
        repository.delete(root)
    }
}
