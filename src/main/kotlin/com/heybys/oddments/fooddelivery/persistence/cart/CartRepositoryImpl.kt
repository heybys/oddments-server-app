package com.heybys.oddments.fooddelivery.persistence.cart

import com.heybys.oddments.base.jpa.BaseRepository
import com.heybys.oddments.fooddelivery.domain.cart.Cart
import com.heybys.oddments.fooddelivery.domain.cart.CartId
import com.heybys.oddments.fooddelivery.domain.cart.CartRepository
import org.springframework.stereotype.Repository

@Repository
internal class CartRepositoryImpl(repository: CartJpaRepository) :
    BaseRepository<Cart, CartId, CartJpaRepository>(repository), CartRepository
