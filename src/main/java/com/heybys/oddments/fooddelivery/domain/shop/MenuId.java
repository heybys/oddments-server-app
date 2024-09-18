package com.heybys.oddments.fooddelivery.domain.shop;

import com.heybys.oddments.base.domain.LongTypeIdentifier;
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType;

public class MenuId extends LongTypeIdentifier {

    public MenuId(Long id) {
        super(id);
    }

    public static class MenuIdJavaType extends LongTypeIdentifierJavaType<MenuId> {
        public MenuIdJavaType() {
            super(MenuId.class);
        }
    }
}
