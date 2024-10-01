package com.heybys.oddments.fooddelivery.persistence.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heybys.oddments.fooddelivery.domain.order.Order;
import com.heybys.oddments.fooddelivery.domain.order.OrderId;

interface OrderJpaRepository extends JpaRepository<Order, OrderId> {}
