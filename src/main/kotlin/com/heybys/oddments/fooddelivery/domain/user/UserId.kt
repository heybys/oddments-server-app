package com.heybys.oddments.fooddelivery.domain.user

import com.heybys.oddments.base.domain.LongTypeIdentifier
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType

class UserId(id: Long) : LongTypeIdentifier(id) {

    class UserIdJavaType : LongTypeIdentifierJavaType<UserId>(UserId::class.java)
}
