package com.heybys.oddments.fooddelivery.service.shop

import com.heybys.oddments.fooddelivery.domain.generic.Money
import com.heybys.oddments.fooddelivery.domain.generic.TimePeriod
import com.heybys.oddments.fooddelivery.domain.shop.MenuRepository
import com.heybys.oddments.fooddelivery.domain.shop.Shop
import com.heybys.oddments.fooddelivery.domain.shop.ShopId
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.LocalTime

@Transactional
@SpringBootTest
class ShopServiceTest
@Autowired
constructor(
    private val menuRepository: MenuRepository,
    private val shopRepository: ShopRepository,
) {

    private val shopService = ShopService(menuRepository, shopRepository)

    companion object {
        private lateinit var shop: Shop
        private lateinit var shopId: ShopId

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            val operatingHours =
                mutableMapOf(
                    MONDAY to TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)),
                    TUESDAY to TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)),
                    WEDNESDAY to TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)),
                    THURSDAY to TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)),
                    FRIDAY to TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)),
                )

            shopId = ShopId(1L)
            shop = Shop(shopId, "Test", Money.wons(32L), operatingHours)
        }
    }

    @DisplayName("put_off_one_hour_on_successfully")
    @Test
    fun putOffOneHourOn() {
        // given
        shopRepository.save(shop)
        val oldFridayTimePeriod = shop.operatingHours?.get(FRIDAY)

        // when
        shopService.putOffOneHourOn(shopId, FRIDAY)

        // then
        val found = shopRepository.find(shopId)
        val fridayTimePeriod = found.operatingHours?.get(FRIDAY)

        assertThat(fridayTimePeriod).isEqualTo(oldFridayTimePeriod?.putOffHours(1))
    }
}
