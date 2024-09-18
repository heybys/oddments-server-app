package com.heybys.oddments.fooddelivery.controller;

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
        return User.builder()
                .username(username)
                .contact(Contact.builder().phone(phone).email(email).build())
                .homeAddress(Address.builder()
                        .city(homeCity)
                        .street(homeStreet)
                        .zipCode(homeZipCode)
                        .build())
                .workAddress(Address.builder()
                        .city(workCity)
                        .street(workStreet)
                        .zipCode(workZipCode)
                        .build())
                .build();
    }
}
