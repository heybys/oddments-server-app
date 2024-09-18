package com.heybys.oddments.base.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

public abstract class AggregateRoot<D extends DomainEntity<D, I>, I> extends DomainEntity<D, I> {

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    protected void registerEvent(D event) {
        Assert.notNull(event, "Domain event must not be null");
        this.domainEvents.add(event);
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    @DomainEvents
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
