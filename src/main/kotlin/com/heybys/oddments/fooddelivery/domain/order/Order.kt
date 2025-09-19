package com.heybys.oddments.fooddelivery.domain.order

import com.heybys.oddments.base.domain.AggregateRoot
import com.heybys.oddments.fooddelivery.domain.order.OrderId.OrderIdJavaType
import com.heybys.oddments.fooddelivery.domain.user.UserId
import com.heybys.oddments.fooddelivery.domain.user.UserId.UserIdJavaType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JavaType
import java.time.LocalDateTime

@Suppress("JpaAttributeTypeInspection")
@Entity
@Table(name = "orders")
class Order : AggregateRoot<Order, OrderId> {

    @Id
    @JavaType(OrderIdJavaType::class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: OrderId? = null

    override fun getId(): OrderId? = _id

    @JavaType(UserIdJavaType::class)
    @Column(name = "user_id")
    var userId: UserId? = null

    @Column(name = "ordered_time")
    var orderedTime: LocalDateTime? = null

    constructor()

    constructor(userId: UserId, orderedTime: LocalDateTime) {
        this.userId = userId
        this.orderedTime = orderedTime
    }

    constructor(id: OrderId?, userId: UserId, orderedTime: LocalDateTime) {
        this._id = id
        this.userId = userId
        this.orderedTime = orderedTime
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()
}
