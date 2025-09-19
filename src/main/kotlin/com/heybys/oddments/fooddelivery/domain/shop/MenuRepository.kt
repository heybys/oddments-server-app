package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.Repository

interface MenuRepository : Repository<Menu, MenuId> {

    fun findOpenMenusIn(shopId: ShopId): List<Menu>

    fun find(shopId: ShopId): List<Menu>
}
