package com.heybys.oddments.fooddelivery.domain.shop;

import java.util.List;

import com.heybys.oddments.base.domain.Repository;

public interface ShopRepository extends Repository<Shop, ShopId> {
    List<Shop> getShops();
}
