package com.heybys.oddments.fooddelivery.persistence.order;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.fooddelivery.domain.order.Order;
import com.heybys.oddments.fooddelivery.domain.order.OrderId;
import com.heybys.oddments.fooddelivery.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
class OrderRepositoryImpl extends BaseRepository<Order, OrderId, OrderJpaRepository> implements OrderRepository {

    public OrderRepositoryImpl(OrderJpaRepository repository) {
        super(repository);
    }
}
