package com.heybys.oddments.fooddelivery.controller.user;

import com.heybys.oddments.fooddelivery.domain.generic.Money;
import com.heybys.oddments.fooddelivery.domain.generic.TimePeriod;
import com.heybys.oddments.fooddelivery.domain.shop.Menu;
import com.heybys.oddments.fooddelivery.domain.shop.MenuRepository;
import com.heybys.oddments.fooddelivery.domain.shop.Option;
import com.heybys.oddments.fooddelivery.domain.shop.OptionGroup;
import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;
import com.heybys.oddments.fooddelivery.domain.shop.ShopRepository;
import com.heybys.oddments.fooddelivery.service.shop.ShopService;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopService shopService;

    @PostMapping("")
    public ResponseEntity<Void> addShop() {

        ShopId shopId = shopService.registerShopSample();

        URI location = URI.create("/api/v1/shop/" + shopId.longValue());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/operation-hours")
    public ResponseEntity<Void> putOffOneHour() {

        shopService.puttOffOneHourOn(new ShopId(1L), DayOfWeek.MONDAY);

        return ResponseEntity.ok().build();
    }
}
