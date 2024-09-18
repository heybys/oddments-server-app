package com.heybys.oddments.fooddelivery.domain.shop;

import com.heybys.oddments.base.domain.ValueObject;
import com.heybys.oddments.fooddelivery.domain.generic.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Option extends ValueObject<Option> {

    @Column(name = "option_name")
    private String name;

    @Column(name = "option_price")
    private Money price;

    public Option() {}

    public Option(String name, Money price) {
        if (name == null || name.length() < 2) {
            throw new IllegalArgumentException("The option name must be at least 2 characters.");
        }

        if (price == null) {
            throw new NullPointerException("The option price must not be null.");
        }

        this.name = name;
        this.price = price;
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
        return Money.ZERO.equals(price);
    }

    public Option changeName(String name) {
        return new Option(name, this.price);
    }
}
