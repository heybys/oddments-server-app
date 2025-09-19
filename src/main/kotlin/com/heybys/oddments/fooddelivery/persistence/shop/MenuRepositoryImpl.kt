package com.heybys.oddments.fooddelivery.persistence.shop

import com.heybys.oddments.base.jpa.BaseRepository
import com.heybys.oddments.fooddelivery.domain.shop.Menu
import com.heybys.oddments.fooddelivery.domain.shop.MenuId
import com.heybys.oddments.fooddelivery.domain.shop.MenuRepository
import com.heybys.oddments.fooddelivery.domain.shop.ShopId
import org.springframework.stereotype.Repository

@Repository
internal class MenuRepositoryImpl(repository: MenuJpaRepository) :
    BaseRepository<Menu, MenuId, MenuJpaRepository>(repository), MenuRepository {

    override fun findOpenMenusIn(shopId: ShopId): List<Menu> {
        return repository.findByShopIdAndIsOpenIsTrue(shopId)
    }

    override fun find(shopId: ShopId): List<Menu> {
        return repository.findByShopId(shopId)
    }
}
