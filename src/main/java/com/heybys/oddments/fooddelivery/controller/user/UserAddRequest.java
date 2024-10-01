package com.heybys.oddments.fooddelivery.controller.user;

import com.heybys.oddments.fooddelivery.domain.generic.Address;
import com.heybys.oddments.fooddelivery.domain.generic.Contact;
import com.heybys.oddments.fooddelivery.domain.user.User;

import lombok.Data;

@Data
public class UserAddRequest {

    private final String username;
    private final String phone;
    private final String email;
    private final String homeCity;
    private final String homeStreet;
    private final String homeZipCode;
    private final String workCity;
    private final String workStreet;
    private final String workZipCode;

    public User toUserDomain() {
        return new User(username, toContact(), toHomeAddress(), toWorkAddress());
    }

    private Contact toContact() {
        return new Contact(phone, email);
    }

    private Address toHomeAddress() {
        return new Address(homeCity, homeStreet, homeZipCode);
    }

    private Address toWorkAddress() {
        return new Address(workCity, workStreet, workZipCode);
    }
}
