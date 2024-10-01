package com.heybys.oddments.fooddelivery.persistence.shop;

import org.springframework.stereotype.Repository;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository;

@Repository
class ShopRepositoryImpl extends BaseRepository<Shop, ShopId, ShopJpaRepository> implements ShopRepository {

    public ShopRepositoryImpl(ShopJpaRepository repository) {
        super(repository);
    }
}
