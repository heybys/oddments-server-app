package com.heybys.oddments.fooddelivery.domain.cart

import com.heybys.oddments.base.domain.LongTypeIdentifier
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType

class CartId(id: Long) : LongTypeIdentifier(id) {

    class CartIdJavaType : LongTypeIdentifierJavaType<CartId>(CartId::class.java)
}
