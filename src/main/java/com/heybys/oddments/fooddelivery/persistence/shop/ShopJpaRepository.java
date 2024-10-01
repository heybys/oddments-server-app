package com.heybys.oddments.fooddelivery.persistence.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;

interface ShopJpaRepository extends JpaRepository<Shop, ShopId> {}
