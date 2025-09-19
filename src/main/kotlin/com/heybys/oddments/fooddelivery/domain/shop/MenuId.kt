package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.LongTypeIdentifier
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType

class MenuId(id: Long) : LongTypeIdentifier(id) {

    class MenuIdJavaType : LongTypeIdentifierJavaType<MenuId>(MenuId::class.java)
}
