package com.heybys.oddments.fooddelivery.persistence.cart;

import org.springframework.stereotype.Repository;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.fooddelivery.domain.cart.Cart;
import com.heybys.oddments.fooddelivery.domain.cart.CartId;
import com.heybys.oddments.fooddelivery.domain.cart.CartRepository;

@Repository
class CartRepositoryImpl extends BaseRepository<Cart, CartId, CartJpaRepository> implements CartRepository {

    public CartRepositoryImpl(CartJpaRepository repository) {
        super(repository);
    }
}
