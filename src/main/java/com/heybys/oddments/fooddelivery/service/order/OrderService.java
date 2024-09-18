package com.heybys.oddments.fooddelivery.service.order;

import com.heybys.oddments.base.annotations.UseCase;
import com.heybys.oddments.fooddelivery.domain.cart.CartId;
import com.heybys.oddments.fooddelivery.domain.cart.CartRepository;
import com.heybys.oddments.fooddelivery.domain.order.OrderRepository;
import com.heybys.oddments.fooddelivery.domain.user.UserId;
import com.heybys.oddments.fooddelivery.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public void placeOrder(UserId userId, CartId cartId) {
        cartRepository.find(cartId);
    }
}
