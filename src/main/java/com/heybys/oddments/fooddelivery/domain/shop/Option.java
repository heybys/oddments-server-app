package com.heybys.oddments.fooddelivery.domain.shop;

import com.heybys.oddments.base.domain.ValueObject;
import com.heybys.oddments.base.jpa.MoneyConverter;
import com.heybys.oddments.fooddelivery.domain.generic.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Option extends ValueObject<Option> {

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    @Convert(converter = MoneyConverter.class)
    private Money optionPrice;

    public Option() {}

    public Option(String optionName, Money optionPrice) {
        if (optionName == null || optionName.length() < 2) {
            throw new IllegalArgumentException("The option name must be at least 2 characters.");
        }

        if (optionPrice == null) {
            throw new NullPointerException("The option price must not be null.");
        }

        this.optionName = optionName;
        this.optionPrice = optionPrice;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isFree() {
        return Money.ZERO.equals(optionPrice);
    }

    public Option changeName(String name) {
        return new Option(name, this.optionPrice);
    }
}
