package com.heybys.oddments.fooddelivery.persistence.shop;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.fooddelivery.domain.shop.Menu;
import com.heybys.oddments.fooddelivery.domain.shop.MenuId;
import com.heybys.oddments.fooddelivery.domain.shop.MenuRepository;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId;

@Repository
class MenuRepositoryImpl extends BaseRepository<Menu, MenuId, MenuJpaRepository> implements MenuRepository {

    public MenuRepositoryImpl(MenuJpaRepository repository) {
        super(repository);
    }

    @Override
    public List<Menu> findOpenMenusIn(ShopId shopId) {
        return repository.findByShopIdAndOpenIsTrue(shopId);
    }

    @Override
    public List<Menu> find(ShopId shopId) {
        return repository.findByShopId(shopId);
    }
}
