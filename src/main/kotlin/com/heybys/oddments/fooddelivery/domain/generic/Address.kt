package com.heybys.oddments.fooddelivery.domain.generic

import com.heybys.oddments.base.domain.ValueObject
import jakarta.persistence.Embeddable

@Embeddable
class Address(val city: String? = null, val street: String? = null, val zipCode: String? = null) :
    ValueObject<Address>() {

    // JPA constructor
    constructor() : this(null, null, null)

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()
}
