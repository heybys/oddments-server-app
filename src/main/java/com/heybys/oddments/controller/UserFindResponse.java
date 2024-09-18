package com.heybys.oddments.controller;

import com.heybys.oddments.domain.generic.Address;
import com.heybys.oddments.domain.generic.Contact;
import com.heybys.oddments.domain.user.User;
import lombok.Data;

@Data
public class UserFindResponse {

    private final String username;

    private final Contact contact;

    private final Address homeAddress;

    private final Address workAddress;

    public static UserFindResponse of(User user) {
        return new UserFindResponse(
                user.getUsername(), user.getContact(), user.getHomeAddress(), user.getWorkAddress());
    }
}
