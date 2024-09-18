package com.heybys.oddments.base.domain;

public abstract class DomainEntity<T extends DomainEntity<T, I>, I> {

    public abstract I getId();

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!other.getClass().equals(getClass())) {
            return false;
        }

        return equals((T) other);
    }

    public boolean equals(T other) {
        if (other == null) {
            return false;
        }

        if (getId() == null) {
            return false;
        }

        if (other.getClass().equals(getClass())) {
            return getId().equals(other.getId());
        }

        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }
}
