package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.LongTypeIdentifier
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType

class OptionGroupId(id: Long) : LongTypeIdentifier(id) {

    class OptionGroupIdJavaType :
        LongTypeIdentifierJavaType<OptionGroupId>(OptionGroupId::class.java)
}
