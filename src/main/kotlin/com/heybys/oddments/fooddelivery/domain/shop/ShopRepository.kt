package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.Repository

interface ShopRepository : Repository<Shop, ShopId> {
    fun getShops(): List<Shop>
}
