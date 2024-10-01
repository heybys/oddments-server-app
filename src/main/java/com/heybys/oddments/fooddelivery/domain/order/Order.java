package com.heybys.oddments.fooddelivery.domain.order;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JavaType;

import com.heybys.oddments.base.domain.AggregateRoot;
import com.heybys.oddments.fooddelivery.domain.order.OrderId.OrderIdJavaType;
import com.heybys.oddments.fooddelivery.domain.user.UserId;
import com.heybys.oddments.fooddelivery.domain.user.UserId.UserIdJavaType;

import lombok.Builder;
import lombok.Getter;

@SuppressWarnings("JpaAttributeTypeInspection")
@Getter
@Entity
@Table(name = "orders")
public class Order extends AggregateRoot<Order, OrderId> {

    @Id
    @JavaType(OrderIdJavaType.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private OrderId id;

    @JavaType(UserIdJavaType.class)
    @Column(name = "user_id")
    private UserId userId;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    public Order() {}

    @Builder
    public Order(UserId userId, LocalDateTime orderedTime) {
        this.userId = userId;
        this.orderedTime = orderedTime;
    }

    @Builder
    public Order(OrderId id, UserId userId, LocalDateTime orderedTime) {
        this.id = id;
        this.userId = userId;
        this.orderedTime = orderedTime;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
