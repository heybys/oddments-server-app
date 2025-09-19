package com.heybys.oddments.fooddelivery.domain.generic

import com.heybys.oddments.base.domain.ValueObject
import jakarta.persistence.Embeddable
import java.time.LocalTime

@Embeddable
class TimePeriod(val startTime: LocalTime? = null, val endTime: LocalTime? = null) :
    ValueObject<TimePeriod>() {

    // JPA constructor
    constructor() : this(null, null)

    companion object {
        @JvmStatic
        fun between(startTime: LocalTime, endTime: LocalTime): TimePeriod =
            TimePeriod(startTime, endTime)
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()

    fun contains(datetime: LocalTime): Boolean {
        return startTime != null &&
            endTime != null &&
            (datetime.isAfter(startTime) || datetime == startTime) &&
            (datetime.isBefore(endTime) || datetime == endTime)
    }

    fun putOffHours(hours: Int): TimePeriod {
        return if (startTime != null && endTime != null) {
            TimePeriod(startTime.plusHours(hours.toLong()), endTime.plusHours(hours.toLong()))
        } else {
            this
        }
    }

    fun putOffHours(hours: Long): TimePeriod {
        return if (startTime != null && endTime != null) {
            TimePeriod(startTime.plusHours(hours), endTime.plusHours(hours))
        } else {
            this
        }
    }
}
