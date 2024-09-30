package com.heybys.oddments.fooddelivery.persistence.shop;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository;
import org.springframework.stereotype.Repository;

@Repository
class ShopRepositoryImpl extends BaseRepository<Shop, ShopId, ShopJpaRepository> implements ShopRepository {

    public ShopRepositoryImpl(ShopJpaRepository repository) {
        super(repository);
    }
}
