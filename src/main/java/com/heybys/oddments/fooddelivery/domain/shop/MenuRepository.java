package com.heybys.oddments.fooddelivery.domain.shop;

import com.heybys.oddments.base.domain.Repository;
import java.util.List;

public interface MenuRepository extends Repository<Menu, MenuId> {

    List<Menu> findOpenMenusIn(ShopId shopId);

    List<Menu> find(ShopId shopId);
}
