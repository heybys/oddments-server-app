package com.heybys.oddments.fooddelivery.service.shop

import com.heybys.oddments.base.annotations.UseCase
import com.heybys.oddments.fooddelivery.domain.generic.Money
import com.heybys.oddments.fooddelivery.domain.generic.TimePeriod
import com.heybys.oddments.fooddelivery.domain.shop.Menu
import com.heybys.oddments.fooddelivery.domain.shop.MenuRepository
import com.heybys.oddments.fooddelivery.domain.shop.Option
import com.heybys.oddments.fooddelivery.domain.shop.OptionGroup
import com.heybys.oddments.fooddelivery.domain.shop.Shop
import com.heybys.oddments.fooddelivery.domain.shop.ShopId
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalTime

@UseCase
class ShopService(
    private val menuRepository: MenuRepository,
    private val shopRepository: ShopRepository,
) {

    private val log = LoggerFactory.getLogger(ShopService::class.java)

    @Transactional
    fun registerShopSample(): ShopId {
        val operatingHours = mutableMapOf<DayOfWeek, TimePeriod>()
        operatingHours[DayOfWeek.MONDAY] = TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0))
        operatingHours[DayOfWeek.TUESDAY] = TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0))
        operatingHours[DayOfWeek.WEDNESDAY] =
            TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0))

        val shop = Shop("Test", Money.wons(32L), operatingHours)
        shopRepository.save(shop)

        val option1 = Option("option1", Money.wons(10L))
        val option2 = Option("option2", Money.wons(20L))

        val optionGroup1 = OptionGroup("optionGroup1", true, option1)

        val menu = Menu(shop.getId()!!, "menu1", "desc1")
        menu.addOptionGroup(optionGroup1)
        menuRepository.save(menu)

        return shop.getId()!!
    }

    fun getShops(): List<Shop> {
        return shopRepository.getShops()
    }

    fun registerShop() {
        // shopRepository.add()
    }

    @Transactional
    fun putOffOneHourOn(shopId: ShopId, dayOfWeek: DayOfWeek) {
        val shop = shopRepository.find(shopId)
        shop.putOffOneHourOn(dayOfWeek)

        log.info(shop.name)
    }
}
