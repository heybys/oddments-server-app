package com.heybys.oddments.fooddelivery.domain.cart;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JavaType;

import com.heybys.oddments.base.domain.AggregateRoot;
import com.heybys.oddments.fooddelivery.domain.cart.CartId.CartIdJavaType;
import com.heybys.oddments.fooddelivery.domain.user.UserId;
import com.heybys.oddments.fooddelivery.domain.user.UserId.UserIdJavaType;

import lombok.Builder;
import lombok.Getter;

@SuppressWarnings("JpaAttributeTypeInspection")
@Getter
@Entity
@Table(name = "cart")
public class Cart extends AggregateRoot<Cart, CartId> {

    @Id
    @JavaType(CartIdJavaType.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private CartId id;

    @JavaType(UserIdJavaType.class)
    private UserId userId;

    public Cart() {}

    @Builder
    public Cart(UserId userId) {
        this.userId = userId;
    }

    @Builder
    public Cart(CartId id, UserId userId) {
        this.id = id;
        this.userId = userId;
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
