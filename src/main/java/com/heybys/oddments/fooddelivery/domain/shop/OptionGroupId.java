package com.heybys.oddments.fooddelivery.domain.shop;

import com.heybys.oddments.base.domain.LongTypeIdentifier;
import com.heybys.oddments.base.jpa.hibernate.LongTypeIdentifierJavaType;

public class OptionGroupId extends LongTypeIdentifier {

    protected OptionGroupId(Long id) {
        super(id);
    }

    public static class OptionGroupIdJavaType extends LongTypeIdentifierJavaType<OptionGroupId> {
        public OptionGroupIdJavaType() {
            super(OptionGroupId.class);
        }
    }
}
