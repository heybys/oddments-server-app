package com.heybys.oddments.fooddelivery.domain.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.heybys.oddments.base.domain.AggregateRoot
import com.heybys.oddments.fooddelivery.domain.generic.AuthProvider
import com.heybys.oddments.fooddelivery.domain.user.UserId.UserIdJavaType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JavaType

@Suppress("JpaAttributeTypeInspection")
@Entity
@Table(name = "user", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class User : AggregateRoot<User, UserId> {

    @Id
    @JavaType(UserIdJavaType::class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: UserId? = null

    override fun getId(): UserId? = _id

    @Column(nullable = false)
    var name: String? = null

    @JsonIgnore var password: String? = null

    @Email
    @Column(nullable = false)
    var email: String? = null

    var imageUrl: String? = null

    @Column(nullable = false, columnDefinition = "TINYINT")
    var emailVerified: Boolean = false

    @NotNull
    @Enumerated(EnumType.STRING)
    var provider: AuthProvider? = null

    var providerId: String? = null

    constructor()

    constructor(
        name: String?,
        password: String?,
        email: String?,
        imageUrl: String?,
        emailVerified: Boolean,
        provider: AuthProvider,
        providerId: String?,
    ) : this(null, name, password, email, imageUrl, emailVerified, provider, providerId)

    constructor(
        id: UserId?,
        name: String?,
        password: String?,
        email: String?,
        imageUrl: String?,
        emailVerified: Boolean,
        provider: AuthProvider,
        providerId: String?,
    ) {
        this._id = id
        this.name = name
        this.password = password
        this.email = email
        this.imageUrl = imageUrl
        this.emailVerified = emailVerified
        this.provider = provider
        this.providerId = providerId
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()
}
