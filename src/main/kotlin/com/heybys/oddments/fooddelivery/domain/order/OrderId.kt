package com.heybys.oddments.fooddelivery.domain.order

import com.heybys.oddments.base.domain.LongTypeIdentifier
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType

class OrderId(id: Long) : LongTypeIdentifier(id) {

    class OrderIdJavaType : LongTypeIdentifierJavaType<OrderId>(OrderId::class.java)
}
