package com.heybys.oddments.fooddelivery.domain.generic

import com.heybys.oddments.base.domain.ValueObject
import jakarta.persistence.Embeddable

@Embeddable
class Contact(val phone: String? = null, val email: String? = null) : ValueObject<Contact>() {

    // JPA constructor
    constructor() : this(null, null)

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()
}
