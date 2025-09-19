package com.heybys.oddments.base.jpa.hibernate

import com.heybys.oddments.base.domain.LongTypeIdentifier
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractClassJavaType
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan
import org.hibernate.type.descriptor.java.MutabilityPlan
import org.hibernate.type.descriptor.jdbc.JdbcType
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators
import java.sql.Types

abstract class LongTypeIdentifierJavaType<T : LongTypeIdentifier>(clazz: Class<T>) :
    AbstractClassJavaType<T>(clazz) {

    override fun toString(value: T?): String? {
        return value?.longValue()?.toString()
    }

    override fun fromString(string: CharSequence?): T {
        return try {
            val longValue =
                string?.toString()?.toLong()
                    ?: throw IllegalArgumentException("Cannot parse null string to Long")
            javaType.getDeclaredConstructor(Long::class.java).newInstance(longValue)
        } catch (ex: Exception) {
            throw IllegalStateException(ex)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <X> unwrap(value: T?, type: Class<X>, options: WrapperOptions?): X? {
        if (value == null) return null

        return if (Long::class.java.isAssignableFrom(type)) {
            value.longValue() as X
        } else {
            throw unknownUnwrap(type)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <X> wrap(value: X?, options: WrapperOptions?): T? {
        if (value == null) return null

        return when {
            javaType.isInstance(value) -> value as T
            value is Long ->
                try {
                    javaType.getDeclaredConstructor(Long::class.java).newInstance(value)
                } catch (e: Exception) {
                    throw IllegalStateException(e)
                }
            else -> throw unknownWrap(value.javaClass)
        }
    }

    override fun getMutabilityPlan(): MutabilityPlan<T> {
        return ImmutableMutabilityPlan.instance()
    }

    override fun getRecommendedJdbcType(indicators: JdbcTypeIndicators): JdbcType {
        return indicators.typeConfiguration.jdbcTypeRegistry.getDescriptor(Types.BIGINT)
    }
}
