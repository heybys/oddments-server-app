package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.LongTypeIdentifier
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType

class ShopId(id: Long) : LongTypeIdentifier(id) {

    class ShopIdJavaType : LongTypeIdentifierJavaType<ShopId>(ShopId::class.java)
}
