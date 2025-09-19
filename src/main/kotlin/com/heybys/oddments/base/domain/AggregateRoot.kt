package com.heybys.oddments.base.domain

import org.springframework.data.annotation.Transient
import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.DomainEvents
import org.springframework.util.Assert
import java.util.Collections

abstract class AggregateRoot<D : DomainEntity<D, I>, I> : DomainEntity<D, I>() {

    @Transient private val domainEvents: MutableList<Any> = mutableListOf()

    protected fun registerEvent(event: D) {
        Assert.notNull(event, "Domain event must not be null")
        domainEvents.add(event)
    }

    @AfterDomainEventPublication
    protected fun clearDomainEvents() {
        domainEvents.clear()
    }

    @DomainEvents
    protected fun domainEvents(): Collection<Any> {
        return Collections.unmodifiableList(domainEvents)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
