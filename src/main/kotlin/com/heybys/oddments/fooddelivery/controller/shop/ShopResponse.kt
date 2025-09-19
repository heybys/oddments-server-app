package com.heybys.oddments.fooddelivery.controller.shop

import com.heybys.oddments.fooddelivery.domain.shop.Shop
import java.math.BigDecimal

data class ShopResponse(val shopId: Long, val shopName: String, val shopMinOrderPrice: BigDecimal) {

    companion object {
        @JvmStatic
        fun of(shop: Shop): ShopResponse {
            return ShopResponse(
                shopId = shop.getId()!!.longValue(),
                shopName = shop.name!!,
                shopMinOrderPrice = shop.minOrderPrice!!.amount,
            )
        }
    }
}
