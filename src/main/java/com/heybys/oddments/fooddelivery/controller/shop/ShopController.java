package com.heybys.oddments.fooddelivery.controller.shop;

import java.net.URI;
import java.time.DayOfWeek;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heybys.oddments.fooddelivery.domain.shop.Shop;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;
import com.heybys.oddments.fooddelivery.service.shop.ShopService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @GetMapping("")
    public ResponseEntity<List<ShopResponse>> getShops() {
        List<Shop> shops = shopService.getShops();

        return ResponseEntity.ok(shops.stream().map(ShopResponse::of).toList());
    }

    @PatchMapping("/operation-hours")
    public ResponseEntity<Void> putOffOneHour() {

        shopService.putOffOneHourOn(new ShopId(1L), DayOfWeek.MONDAY);

        return ResponseEntity.ok().build();
    }
}
