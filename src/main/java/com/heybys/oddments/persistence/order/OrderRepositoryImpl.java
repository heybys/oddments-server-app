package com.heybys.oddments.persistence.order;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.domain.order.Order;
import com.heybys.oddments.domain.order.OrderId;
import com.heybys.oddments.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl extends BaseRepository<Order, OrderId, OrderJpaRepository> implements OrderRepository {

    public OrderRepositoryImpl(OrderJpaRepository repository) {
        super(repository);
    }
}
