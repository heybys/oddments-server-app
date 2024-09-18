package com.heybys.oddments.base.domain;

public abstract class LongTypeIdentifier extends ValueObject<LongTypeIdentifier> {

    private final Long id;

    protected LongTypeIdentifier(Long id) {
        this.id = id;
    }

    public Long longValue() {
        return id;
    }

    public Long nextValue() {
        return id + 1;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    protected Object[] getEqualityFields() {
        return new Object[] {id};
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " : " + longValue();
    }
}
