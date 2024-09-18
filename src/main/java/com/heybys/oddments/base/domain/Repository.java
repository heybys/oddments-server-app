package com.heybys.oddments.base.domain;

public interface Repository<D extends AggregateRoot<D, I>, I> {
    void add(D root);

    D find(I id);

    void remove(I id);

    void remove(D root);
}
