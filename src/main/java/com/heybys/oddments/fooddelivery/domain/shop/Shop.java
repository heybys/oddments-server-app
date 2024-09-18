package com.heybys.oddments.fooddelivery.domain.shop;

import com.heybys.oddments.base.domain.AggregateRoot;
import com.heybys.oddments.fooddelivery.domain.generic.Money;
import com.heybys.oddments.fooddelivery.domain.generic.TimePeriod;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId.ShopIdJavaType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import org.hibernate.annotations.JavaType;

@Getter
@Entity
@Table(name = "shop")
public class Shop extends AggregateRoot<Shop, ShopId> {

    @Id
    @JavaType(ShopIdJavaType.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ShopId id;

    @Column(name = "shop_name")
    private String name;

    @Column(name = "min_order_amount")
    private Money minOrderPrice;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "shop_operation_hours", joinColumns = @JoinColumn(name = "shop_id"))
    @MapKeyColumn(name = "day_of_week")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<DayOfWeek, TimePeriod> operatingHours;

    public Shop() {}

    public Shop(String name, Money minOrderPrice, Map<DayOfWeek, TimePeriod> operatingHours) {
        this(null, name, minOrderPrice, operatingHours);
    }

    public Shop(ShopId id, String name, Money minOrderPrice, Map<DayOfWeek, TimePeriod> operatingHours) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.operatingHours = operatingHours;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isOpen() {
        return isOpen(LocalDateTime.now());
    }

    public boolean isOpen(LocalDateTime time) {
        if (!operatingHours.containsKey(time.getDayOfWeek())) {
            return false;
        }

        return operatingHours.get(time.getDayOfWeek()).contains(time.toLocalTime());
    }

    public void putOffOneHourOn(DayOfWeek dayOfWeek) {
        operatingHours.computeIfPresent(dayOfWeek, (k, period) -> period.putOffHours(1));
    }
}
