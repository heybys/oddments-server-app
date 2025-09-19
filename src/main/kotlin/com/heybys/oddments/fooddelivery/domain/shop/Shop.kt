package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.AggregateRoot
import com.heybys.oddments.base.jpa.MoneyConverter
import com.heybys.oddments.fooddelivery.domain.generic.Money
import com.heybys.oddments.fooddelivery.domain.generic.TimePeriod
import com.heybys.oddments.fooddelivery.domain.shop.ShopId.ShopIdJavaType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapKeyColumn
import jakarta.persistence.MapKeyEnumerated
import jakarta.persistence.Table
import org.hibernate.annotations.JavaType
import java.time.DayOfWeek
import java.time.LocalDateTime

@Suppress("JpaAttributeTypeInspection")
@Entity
@Table(name = "shop")
class Shop : AggregateRoot<Shop, ShopId> {

    @Id
    @JavaType(ShopIdJavaType::class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: ShopId? = null

    override fun getId(): ShopId? = _id

    @Column(name = "shop_name")
    var name: String? = null

    @Convert(converter = MoneyConverter::class)
    @Column(name = "min_order_amount")
    var minOrderPrice: Money? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "shop_operation_hours", joinColumns = [JoinColumn(name = "shop_id")])
    @MapKeyColumn(name = "day_of_week")
    @MapKeyEnumerated(EnumType.STRING)
    var operatingHours: MutableMap<DayOfWeek, TimePeriod>? = null

    constructor()

    constructor(
        name: String,
        minOrderPrice: Money,
        operatingHours: MutableMap<DayOfWeek, TimePeriod>,
    ) : this(null, name, minOrderPrice, operatingHours)

    constructor(
        id: ShopId?,
        name: String,
        minOrderPrice: Money,
        operatingHours: MutableMap<DayOfWeek, TimePeriod>,
    ) {
        this._id = id
        this.name = name
        this.minOrderPrice = minOrderPrice
        this.operatingHours = operatingHours
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()

    fun isOpen(): Boolean = isOpen(LocalDateTime.now())

    fun isOpen(time: LocalDateTime): Boolean {
        val hours = operatingHours ?: return false
        val dayOfWeek = time.dayOfWeek

        if (!hours.containsKey(dayOfWeek)) {
            return false
        }

        return hours[dayOfWeek]?.contains(time.toLocalTime()) ?: false
    }

    fun putOffOneHourOn(dayOfWeek: DayOfWeek) {
        operatingHours?.computeIfPresent(dayOfWeek) { _, period -> period.putOffHours(1) }
    }
}
