package com.heybys.oddments.base.domain;

import java.util.Arrays;

public abstract class ValueObject<T extends ValueObject<T>> {

    @Override
    @SuppressWarnings("unchecked")
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

        return Arrays.equals(getEqualityFields(), other.getEqualityFields());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        for (Object each : getEqualityFields()) {
            hash = hash * 31 + (each == null ? 0 : each.hashCode());
        }
        return hash;
    }

    @SuppressWarnings({"java:S3011", "java:S112"})
    protected Object[] getEqualityFields() {
        return Arrays.stream(getClass().getDeclaredFields())
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(this);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray();
    }
}
