package com.heybys.oddments.base.jpa.hibernate;

import com.heybys.oddments.base.domain.LongTypeIdentifier;
import java.sql.Types;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;
import org.hibernate.type.descriptor.java.MutabilityPlan;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;

public abstract class LongTypeIdentifierJavaType<T extends LongTypeIdentifier> extends AbstractClassJavaType<T> {

    protected LongTypeIdentifierJavaType(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public String toString(T value) {
        return value == null ? null : value.longValue().toString();
    }

    @Override
    public T fromString(CharSequence string) {
        try {
            return getJavaType().getDeclaredConstructor(Long.class).newInstance(Long.valueOf(string.toString()));
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (Long.class.isAssignableFrom(type)) {
            return (X) value.longValue();
        }

        throw this.unknownUnwrap(type);
    }

    @SuppressWarnings("unchecked")
    public <X> T wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (getJavaType().isInstance(value)) {
            return (T) value;
        }

        if (value instanceof Long longValue) {
            try {
                return getJavaType().getDeclaredConstructor(Long.class).newInstance(longValue);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        throw unknownWrap(value.getClass());
    }

    @Override
    public MutabilityPlan<T> getMutabilityPlan() {
        return ImmutableMutabilityPlan.instance();
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return indicators.getTypeConfiguration().getJdbcTypeRegistry().getDescriptor(Types.BIGINT);
    }
}
