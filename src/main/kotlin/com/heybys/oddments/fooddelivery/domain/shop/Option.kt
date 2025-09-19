package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.ValueObject
import com.heybys.oddments.base.jpa.MoneyConverter
import com.heybys.oddments.fooddelivery.domain.generic.Money
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable

@Embeddable
class Option(
    @Column(nullable = false) val optionName: String? = null,
    @Column(nullable = false)
    @Convert(converter = MoneyConverter::class)
    val optionPrice: Money? = null,
) : ValueObject<Option>() {

    // JPA constructor
    constructor() : this(null, null)

    init {
        if (optionName != null && optionName.length < 2) {
            throw IllegalArgumentException("The option name must be at least 2 characters.")
        }

        if (optionName != null && optionPrice == null) {
            throw NullPointerException("The option price must not be null.")
        }
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()

    fun isFree(): Boolean = Money.ZERO == optionPrice

    fun changeName(name: String): Option = Option(name, this.optionPrice)
}
