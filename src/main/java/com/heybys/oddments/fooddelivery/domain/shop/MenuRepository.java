package com.heybys.oddments.fooddelivery.domain.shop;

import java.util.List;

import com.heybys.oddments.base.domain.Repository;

public interface MenuRepository extends Repository<Menu, MenuId> {

    List<Menu> findOpenMenusIn(ShopId shopId);

    List<Menu> find(ShopId shopId);
}
