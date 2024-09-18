package com.heybys.oddments.fooddelivery.domain.generic;

import com.heybys.oddments.base.domain.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Contact extends ValueObject<Contact> {

    private String phone;

    private String email;

    public Contact() {}

    public Contact(String phone, String email) {
        this.phone = phone;
        this.email = email;
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
