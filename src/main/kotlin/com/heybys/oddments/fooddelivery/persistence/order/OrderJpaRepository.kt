package com.heybys.oddments.fooddelivery.persistence.order

import com.heybys.oddments.fooddelivery.domain.order.Order
import com.heybys.oddments.fooddelivery.domain.order.OrderId
import org.springframework.data.jpa.repository.JpaRepository

internal interface OrderJpaRepository : JpaRepository<Order, OrderId>
