package com.heybys.oddments.fooddelivery.controller.shop

import com.heybys.oddments.fooddelivery.domain.shop.ShopId
import com.heybys.oddments.fooddelivery.service.shop.ShopService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.DayOfWeek

@RestController
@RequestMapping("/api/v1/shop")
class ShopController(private val shopService: ShopService) {

    private val log = LoggerFactory.getLogger(ShopController::class.java)

    @PostMapping("")
    fun addShop(): ResponseEntity<Void> {
        val shopId = shopService.registerShopSample()

        val location = URI.create("/api/v1/shop/" + shopId.longValue())
        return ResponseEntity.created(location).build()
    }

    @GetMapping("")
    fun getShops(): ResponseEntity<List<ShopResponse>> {
        val shops = shopService.getShops()

        return ResponseEntity.ok(shops.map { ShopResponse.of(it) })
    }

    @PatchMapping("/operation-hours")
    fun putOffOneHour(): ResponseEntity<Void> {
        shopService.putOffOneHourOn(ShopId(1L), DayOfWeek.MONDAY)

        return ResponseEntity.ok().build()
    }
}
