package com.heybys.oddments.domain.user;

import com.heybys.oddments.base.domain.LongTypeIdentifier;
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType;

public class UserId extends LongTypeIdentifier {

    public UserId(Long id) {
        super(id);
    }

    public static class UserIdJavaType extends LongTypeIdentifierJavaType<UserId> {
        public UserIdJavaType() {
            super(UserId.class);
        }
    }
}
