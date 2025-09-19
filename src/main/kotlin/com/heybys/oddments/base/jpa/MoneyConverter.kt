package com.heybys.oddments.base.jpa

import com.heybys.oddments.fooddelivery.domain.generic.Money
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.math.BigDecimal

@Converter(autoApply = true)
class MoneyConverter : AttributeConverter<Money, BigDecimal> {

    override fun convertToDatabaseColumn(money: Money?): BigDecimal? {
        return money?.amount
    }

    override fun convertToEntityAttribute(amount: BigDecimal?): Money? {
        return amount?.let { Money(it) }
    }
}
