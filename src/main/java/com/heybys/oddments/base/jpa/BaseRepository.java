package com.heybys.oddments.base.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heybys.oddments.base.domain.AggregateRoot;
import com.heybys.oddments.base.domain.Repository;

public abstract class BaseRepository<D extends AggregateRoot<D, I>, I, R extends JpaRepository<D, I>>
        implements Repository<D, I> {

    protected R repository;

    protected BaseRepository(R repository) {
        this.repository = repository;
    }

    @Override
    public D save(D root) {
        return repository.save(root);
    }

    @Override
    public D find(I id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void remove(I id) {
        repository.deleteById(id);
    }

    @Override
    public void remove(D root) {
        repository.delete(root);
    }
}
