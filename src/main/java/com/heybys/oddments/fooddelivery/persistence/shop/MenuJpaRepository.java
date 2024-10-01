package com.heybys.oddments.fooddelivery.persistence.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heybys.oddments.fooddelivery.domain.shop.Menu;
import com.heybys.oddments.fooddelivery.domain.shop.MenuId;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;

interface MenuJpaRepository extends JpaRepository<Menu, MenuId> {

    List<Menu> findByShopIdAndOpenIsTrue(ShopId shopId);

    List<Menu> findByShopId(ShopId shopId);
}
