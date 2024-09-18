package com.heybys.oddments.fooddelivery.domain.generic;

import com.heybys.oddments.base.domain.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Address extends ValueObject<Address> {

    private String city;

    private String street;

    private String zipCode;

    public Address() {}

    public Address(String city, String street, String zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
