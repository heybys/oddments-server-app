package com.heybys.oddments.fooddelivery.persistence.cart;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.fooddelivery.domain.cart.Cart;
import com.heybys.oddments.fooddelivery.domain.cart.CartId;
import com.heybys.oddments.fooddelivery.domain.cart.CartRepository;
import org.springframework.stereotype.Repository;

@Repository
class CartRepositoryImpl extends BaseRepository<Cart, CartId, CartJpaRepository> implements CartRepository {

    public CartRepositoryImpl(CartJpaRepository repository) {
        super(repository);
    }
}
