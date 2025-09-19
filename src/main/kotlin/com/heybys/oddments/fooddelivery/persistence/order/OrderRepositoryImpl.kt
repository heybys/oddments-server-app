package com.heybys.oddments.fooddelivery.persistence.order

import com.heybys.oddments.base.jpa.BaseRepository
import com.heybys.oddments.fooddelivery.domain.order.Order
import com.heybys.oddments.fooddelivery.domain.order.OrderId
import com.heybys.oddments.fooddelivery.domain.order.OrderRepository
import org.springframework.stereotype.Repository

@Repository
internal class OrderRepositoryImpl(repository: OrderJpaRepository) :
    BaseRepository<Order, OrderId, OrderJpaRepository>(repository), OrderRepository
