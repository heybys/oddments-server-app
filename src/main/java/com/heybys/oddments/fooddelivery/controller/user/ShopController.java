package com.heybys.oddments.fooddelivery.controller.user;

import com.heybys.oddments.fooddelivery.domain.generic.Money;
import com.heybys.oddments.fooddelivery.domain.generic.TimePeriod;
import com.heybys.oddments.fooddelivery.domain.shop.Menu;
import com.heybys.oddments.fooddelivery.domain.shop.MenuRepository;
import com.heybys.oddments.fooddelivery.domain.shop.Option;
import com.heybys.oddments.fooddelivery.domain.shop.OptionGroup;
import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;

    public ShopController(ShopRepository shopRepository, MenuRepository menuRepository) {
        this.shopRepository = shopRepository;
        this.menuRepository = menuRepository;
    }

    @PostMapping("")
    public ResponseEntity<Void> addShop() {

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

        URI location = URI.create("/api/v1/shop/" + shop.getId().longValue());

        return ResponseEntity.created(location).build();
    }
}
