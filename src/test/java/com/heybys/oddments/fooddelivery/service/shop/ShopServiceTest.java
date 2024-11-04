package com.heybys.oddments.fooddelivery.service.shop;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;

import com.heybys.oddments.fooddelivery.domain.generic.Money;
import com.heybys.oddments.fooddelivery.domain.generic.TimePeriod;
import com.heybys.oddments.fooddelivery.domain.shop.MenuRepository;
import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ShopServiceTest {

    private static Shop shop;

    private static ShopId shopId;

    private final ShopRepository shopRepository;

    private final MenuRepository menuRepository;

    private final ShopService shopService;

    @Autowired
    public ShopServiceTest(MenuRepository menuRepository, ShopRepository shopRepository) {
        this.menuRepository = menuRepository;
        this.shopRepository = shopRepository;
        this.shopService = new ShopService(menuRepository, shopRepository);
    }

    @BeforeAll
    static void beforeAll() {
        Map<DayOfWeek, TimePeriod> operatingHours = new HashMap<>();
        operatingHours.put(MONDAY, TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        operatingHours.put(TUESDAY, TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        operatingHours.put(WEDNESDAY, TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        operatingHours.put(THURSDAY, TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        operatingHours.put(FRIDAY, TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)));

        shopId = new ShopId(1L);

        shop = new Shop(shopId, "Test", Money.wons(32L), operatingHours);
    }

    @DisplayName("put_off_one_hour_on_successfully")
    @Test
    void putOffOneHourOn() {
        // given
        shopRepository.add(shop);
        TimePeriod oldFridayTimePeriod = shop.getOperatingHours().get(FRIDAY);

        // when
        shopService.putOffOneHourOn(shopId, FRIDAY);

        // then
        Shop found = shopRepository.find(shopId);
        TimePeriod fridayTimePeriod = found.getOperatingHours().get(FRIDAY);

        assertThat(fridayTimePeriod).isEqualTo(oldFridayTimePeriod.putOffHours(1));
    }
}