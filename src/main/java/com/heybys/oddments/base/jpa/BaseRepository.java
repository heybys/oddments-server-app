package com.heybys.oddments.base.jpa;

import com.heybys.oddments.base.domain.AggregateRoot;
import com.heybys.oddments.base.domain.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseRepository<D extends AggregateRoot<D, I>, I, R extends JpaRepository<D, I>>
        implements Repository<D, I> {

    protected R repository;

    protected BaseRepository(R repository) {
        this.repository = repository;
    }

    @Override
    public void add(D root) {
        repository.save(root);
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