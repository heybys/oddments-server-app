package com.heybys.oddments.fooddelivery.persistence.shop

import com.heybys.oddments.base.jpa.BaseRepository
import com.heybys.oddments.fooddelivery.domain.shop.Shop
import com.heybys.oddments.fooddelivery.domain.shop.ShopId
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository
import org.springframework.stereotype.Repository

@Repository
internal class ShopRepositoryImpl(repository: ShopJpaRepository) :
    BaseRepository<Shop, ShopId, ShopJpaRepository>(repository), ShopRepository {

    override fun getShops(): List<Shop> {
        return repository.findAll()
    }
}
