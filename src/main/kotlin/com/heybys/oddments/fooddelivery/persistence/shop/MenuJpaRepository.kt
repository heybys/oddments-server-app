package com.heybys.oddments.fooddelivery.persistence.shop

import com.heybys.oddments.fooddelivery.domain.shop.Menu
import com.heybys.oddments.fooddelivery.domain.shop.MenuId
import com.heybys.oddments.fooddelivery.domain.shop.ShopId
import org.springframework.data.jpa.repository.JpaRepository

internal interface MenuJpaRepository : JpaRepository<Menu, MenuId> {

    fun findByShopIdAndIsOpenIsTrue(shopId: ShopId): List<Menu>

    fun findByShopId(shopId: ShopId): List<Menu>
}
