package com.heybys.oddments.fooddelivery.controller.user;

import com.heybys.oddments.fooddelivery.domain.generic.Address;
import com.heybys.oddments.fooddelivery.domain.generic.Contact;
import com.heybys.oddments.fooddelivery.domain.user.User;

import lombok.Data;

@Data
public class UserFindResponse {

    private final String username;

    private final Contact contact;

    private final Address homeAddress;

    private final Address workAddress;

    public static UserFindResponse of(User user) {
        return new UserFindResponse(user.getName(), null, null, null);
    }
}
