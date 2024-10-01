package com.heybys.oddments.fooddelivery.domain.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.JavaType;

import com.heybys.oddments.base.domain.AggregateRoot;
import com.heybys.oddments.fooddelivery.domain.shop.MenuId.MenuIdJavaType;
import com.heybys.oddments.fooddelivery.domain.shop.ShopId.ShopIdJavaType;

import lombok.Getter;

@SuppressWarnings("JpaAttributeTypeInspection")
@Getter
@Entity
@Table(name = "menu")
public class Menu extends AggregateRoot<Menu, MenuId> {

    @Id
    @JavaType(MenuIdJavaType.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private MenuId id;

    @JavaType(ShopIdJavaType.class)
    @Column(name = "shop_id")
    private ShopId shopId;

    @Column(name = "menu_name")
    private String name;

    @Column(name = "menu_description")
    private String description;

    @Column(name = "open", columnDefinition = "bit")
    private boolean open;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<OptionGroup> groups = new ArrayList<>();

    public Menu() {}

    public Menu(ShopId shopId, String name, String description) {
        this(null, shopId, name, description, false, new ArrayList<>());
    }

    public Menu(MenuId id, ShopId shopId, String name, String description, boolean open, List<OptionGroup> groups) {
        this.id = id;
        this.shopId = shopId;
        this.name = name;
        this.description = description;
        this.open = open;
        this.groups = groups;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    public void addOptionGroup(OptionGroup optionGroup) {
        if (optionGroup == null) {
            throw new IllegalArgumentException("OptionGroup cannot be null.");
        }

        if (groups.stream().anyMatch(group -> group.getName().equals(optionGroup.getName()))) {
            throw new IllegalArgumentException("An option group with the same name already exists.");
        }

        if (isOpen() && optionGroup.isMandatory() && countOfMandatoryOptionGroups() >= 3) {
            throw new IllegalArgumentException("Up to three required option groups can be registered.");
        }

        groups.add(optionGroup);
    }

    public void open() {
        if (groups.isEmpty()) {
            throw new IllegalStateException("There must be at least one option group.");
        }

        if (countOfMandatoryOptionGroups() == 0) {
            throw new IllegalStateException("At least one required option group must be registered.");
        }

        if (countOfMandatoryOptionGroups() > 3) {
            throw new IllegalStateException("Only up to 3 required option groups can be registered.");
        }

        if (countOfFreeOptionGroups() < 1) {
            throw new IllegalStateException("At least one option group with a set amount must be registered.");
        }

        open = true;
    }

    public void removeOptionGroup(OptionGroupId optionGroupId) {
        if (optionGroupId == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        }

        if (groups.isEmpty()) {
            throw new IllegalArgumentException("The option groups is empty.");
        }

        OptionGroup optionGroup = groups.stream()
                .filter(group -> group.getId().equals(optionGroupId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Option group not found."));

        if (!isOpen()) {
            groups.remove(optionGroup);
            return;
        }

        if (groups.size() == 1) {
            throw new IllegalArgumentException("At least one option group must be registered.");
        }

        if (optionGroup.isMandatory() && countOfMandatoryOptionGroups() == 1) {
            throw new IllegalArgumentException("At least one required option group must be registered.");
        }

        if (!optionGroup.isFree() && countOfFreeOptionGroups() == 1) {
            throw new IllegalArgumentException("At least one option group with a set amount must be registered.");
        }

        groups.remove(optionGroup);
    }

    public void changeOptionGroupName(OptionGroupId optionGroupId, String name) {
        if (optionGroupId == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        }

        if (groups.isEmpty()) {
            throw new IllegalArgumentException("The option groups is empty.");
        }

        if (groups.stream().anyMatch(group -> group.getName().equals(name))) {
            throw new IllegalArgumentException("An option group with the same name already exists.");
        }

        OptionGroup optionGroup = getOptionGroup(optionGroupId)
                .orElseThrow(() -> new IllegalArgumentException("Option group not found."));

        optionGroup.chaneName(name);
    }

    public void changeOptionName(OptionGroupId optionGroupId, Option target, String optionName) {
        if (optionGroupId == null) {
            throw new IllegalArgumentException("The optionGroupId parameter must not be null.");
        }

        if (target == null) {
            throw new IllegalArgumentException("The target Option parameter must not be null.");
        }

        if (groups.isEmpty()) {
            throw new IllegalArgumentException("The option groups is empty.");
        }

        OptionGroup optionGroup = getOptionGroup(optionGroupId)
                .orElseThrow(() -> new IllegalArgumentException("Option group not found."));

        optionGroup.changeOptionName(target, optionName);
    }

    private long countOfMandatoryOptionGroups() {
        return groups.stream().filter(OptionGroup::isMandatory).count();
    }

    private long countOfFreeOptionGroups() {
        return groups.stream().filter(group -> !group.isFree()).count();
    }

    private Optional<OptionGroup> getOptionGroup(OptionGroupId optionGroupId) {
        return groups.stream()
                .filter(group -> group.getId().equals(optionGroupId))
                .findFirst();
    }

    public Optional<OptionGroup> getOptionGroup(String name) {
        return groups.stream().filter(group -> group.getName().equals(name)).findFirst();
    }
}
