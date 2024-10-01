package com.heybys.oddments.fooddelivery.persistence.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heybys.oddments.fooddelivery.domain.cart.Cart;
import com.heybys.oddments.fooddelivery.domain.cart.CartId;

interface CartJpaRepository extends JpaRepository<Cart, CartId> {}
