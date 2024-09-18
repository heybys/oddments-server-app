package com.heybys.oddments.fooddelivery.domain.cart;

import com.heybys.oddments.base.domain.LongTypeIdentifier;
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType;

public class CartId extends LongTypeIdentifier {

    public CartId(Long id) {
        super(id);
    }

    public static class CartIdJavaType extends LongTypeIdentifierJavaType<CartId> {
        public CartIdJavaType() {
            super(CartId.class);
        }
    }
}
