package com.heybys.oddments.fooddelivery.persistence.cart

import com.heybys.oddments.fooddelivery.domain.cart.Cart
import com.heybys.oddments.fooddelivery.domain.cart.CartId
import org.springframework.data.jpa.repository.JpaRepository

internal interface CartJpaRepository : JpaRepository<Cart, CartId>
