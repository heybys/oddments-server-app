package com.heybys.oddments.fooddelivery.service.shop;

import com.heybys.oddments.base.annotations.UseCase;
import com.heybys.oddments.fooddelivery.domain.generic.Money;
import com.heybys.oddments.fooddelivery.domain.generic.TimePeriod;
import com.heybys.oddments.fooddelivery.domain.shop.Menu;
import com.heybys.oddments.fooddelivery.domain.shop.MenuRepository;
import com.heybys.oddments.fooddelivery.domain.shop.Option;
import com.heybys.oddments.fooddelivery.domain.shop.OptionGroup;
import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ShopService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    @Transactional
    public ShopId registerShopSample() {

        Map<DayOfWeek, TimePeriod> operatingHours = new HashMap<>();
        operatingHours.put(DayOfWeek.MONDAY, TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        operatingHours.put(DayOfWeek.TUESDAY, TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        operatingHours.put(DayOfWeek.WEDNESDAY, TimePeriod.between(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        Shop shop = new Shop("Test", Money.wons(32L), operatingHours);

        shopRepository.add(shop);

        Option option1 = new Option("option1", Money.wons(10L));
        Option option2 = new Option("option2", Money.wons(20L));

        OptionGroup optionGroup1 = new OptionGroup("optionGroup1", true, option1);

        Menu menu = new Menu(shop.getId(), "menu1", "desc1");
        menu.addOptionGroup(optionGroup1);
        menuRepository.add(menu);

        return shop.getId();
    }

    public void registerShop() {
        // shopRepository.add();
    }

    @Transactional
    public void puttOffOneHourOn(ShopId shopId, DayOfWeek dayOfWeek) {
        Shop shop = shopRepository.find(shopId);
        shop.putOffOneHourOn(dayOfWeek);

        log.info(shop.getName());
    }
}
