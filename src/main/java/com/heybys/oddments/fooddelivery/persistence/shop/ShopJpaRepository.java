package com.heybys.oddments.fooddelivery.persistence.shop;

import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<Shop, ShopId> {}
