package com.heybys.oddments.fooddelivery.service.order

import com.heybys.oddments.base.annotations.UseCase
import com.heybys.oddments.fooddelivery.domain.cart.CartId
import com.heybys.oddments.fooddelivery.domain.cart.CartRepository
import com.heybys.oddments.fooddelivery.domain.order.OrderRepository
import com.heybys.oddments.fooddelivery.domain.user.UserId
import com.heybys.oddments.fooddelivery.domain.user.UserRepository

@UseCase
class OrderService(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
) {

    fun placeOrder(userId: UserId, cartId: CartId) {
        cartRepository.find(cartId)
    }
}
