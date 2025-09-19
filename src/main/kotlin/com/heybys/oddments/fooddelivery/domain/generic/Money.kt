package com.heybys.oddments.fooddelivery.domain.generic

import com.heybys.oddments.base.domain.ValueObject
import java.math.BigDecimal
import java.util.function.Function

class Money(val amount: BigDecimal) : ValueObject<Money>() {

    companion object {
        @JvmField val ZERO = Money.wons(0)

        @JvmStatic fun wons(amount: Long): Money = Money(BigDecimal.valueOf(amount))

        @JvmStatic fun wons(amount: Double): Money = Money(BigDecimal.valueOf(amount))

        @JvmStatic
        fun <T> sum(bags: Collection<T>, monetary: Function<T, Money>): Money =
            bags.map { monetary.apply(it) }.fold(ZERO) { acc, money -> acc.plus(money) }
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()

    override val equalityFields: Array<Any?>
        get() = arrayOf(amount.toDouble())

    fun plus(amount: Money): Money = Money(this.amount.add(amount.amount))

    fun minus(amount: Money): Money = Money(this.amount.subtract(amount.amount))

    fun times(percent: Double): Money = Money(this.amount.multiply(BigDecimal.valueOf(percent)))

    fun divide(divisor: Double): Money = Money(amount.divide(BigDecimal.valueOf(divisor)))

    fun isLessThan(other: Money): Boolean = amount.compareTo(other.amount) < 0

    fun isGreaterThanOrEqual(other: Money): Boolean = amount.compareTo(other.amount) >= 0

    fun longValue(): Long = amount.toLong()

    fun doubleValue(): Double = amount.toDouble()

    override fun toString(): String = "${amount}원"
}
