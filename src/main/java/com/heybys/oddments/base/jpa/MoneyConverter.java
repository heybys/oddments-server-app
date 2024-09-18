package com.heybys.oddments.base.jpa;

import com.heybys.oddments.fooddelivery.domain.generic.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        return money.getAmount();
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal amount) {
        return new Money(amount);
    }
}
