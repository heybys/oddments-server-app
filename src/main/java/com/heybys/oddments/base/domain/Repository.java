package com.heybys.oddments.base.domain;

public interface Repository<D extends AggregateRoot<D, I>, I> {
    D save(D root);

    D find(I id);

    void remove(I id);

    void remove(D root);
}
