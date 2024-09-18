package com.heybys.oddments.fooddelivery.domain.shop;

import com.heybys.oddments.base.domain.LongTypeIdentifier;
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType;

public class ShopId extends LongTypeIdentifier {

    public ShopId(Long id) {
        super(id);
    }

    public static class ShopIdJavaType extends LongTypeIdentifierJavaType<ShopId> {
        public ShopIdJavaType() {
            super(ShopId.class);
        }
    }
}
