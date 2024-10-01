package com.heybys.oddments.fooddelivery.persistence.order;

import org.springframework.stereotype.Repository;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.fooddelivery.domain.order.Order;
import com.heybys.oddments.fooddelivery.domain.order.OrderId;
import com.heybys.oddments.fooddelivery.domain.order.OrderRepository;

@Repository
class OrderRepositoryImpl extends BaseRepository<Order, OrderId, OrderJpaRepository> implements OrderRepository {

    public OrderRepositoryImpl(OrderJpaRepository repository) {
        super(repository);
    }
}
