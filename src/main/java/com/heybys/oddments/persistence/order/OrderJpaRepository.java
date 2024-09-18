package com.heybys.oddments.persistence.order;

import com.heybys.oddments.domain.order.Order;
import com.heybys.oddments.domain.order.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, OrderId> {}
