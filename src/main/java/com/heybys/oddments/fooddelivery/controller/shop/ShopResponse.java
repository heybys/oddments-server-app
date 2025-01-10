package com.heybys.oddments.fooddelivery.controller.shop;

import java.math.BigDecimal;

import com.heybys.oddments.fooddelivery.domain.shop.Shop;

import lombok.Getter;

@Getter
public class ShopResponse {

    private Long shopId;
    private String shopName;
    private BigDecimal shopMinOrderPrice;

    public static ShopResponse of(Shop shop) {
        ShopResponse response = new ShopResponse();
        response.shopId = shop.getId().longValue();
        response.shopName = shop.getName();
        response.shopMinOrderPrice = shop.getMinOrderPrice().getAmount();
        return response;
    }
}
