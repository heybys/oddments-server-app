package com.heybys.oddments.fooddelivery.domain.cart

import com.heybys.oddments.base.domain.AggregateRoot
import com.heybys.oddments.fooddelivery.domain.cart.CartId.CartIdJavaType
import com.heybys.oddments.fooddelivery.domain.user.UserId
import com.heybys.oddments.fooddelivery.domain.user.UserId.UserIdJavaType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JavaType

@Suppress("JpaAttributeTypeInspection")
@Entity
@Table(name = "cart")
class Cart : AggregateRoot<Cart, CartId> {

    @Id
    @JavaType(CartIdJavaType::class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: CartId? = null

    override fun getId(): CartId? = _id

    @JavaType(UserIdJavaType::class)
    var userId: UserId? = null

    constructor()

    constructor(userId: UserId) {
        this.userId = userId
    }

    constructor(id: CartId?, userId: UserId) {
        this._id = id
        this.userId = userId
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()
}
