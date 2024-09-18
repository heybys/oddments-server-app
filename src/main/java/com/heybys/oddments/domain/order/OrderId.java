package com.heybys.oddments.domain.order;

import com.heybys.oddments.base.domain.LongTypeIdentifier;
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType;

public class OrderId extends LongTypeIdentifier {

    public OrderId(Long id) {
        super(id);
    }

    public static class OrderIdJavaType extends LongTypeIdentifierJavaType<OrderId> {
        public OrderIdJavaType() {
            super(OrderId.class);
        }
    }
}
