package com.heybys.oddments.fooddelivery.domain.shop;

import com.heybys.oddments.base.domain.DomainEntity;
import com.heybys.oddments.fooddelivery.domain.shop.MenuId.MenuIdJavaType;
import com.heybys.oddments.fooddelivery.domain.shop.OptionGroupId.OptionGroupIdJavaType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.hibernate.annotations.JavaType;

@SuppressWarnings("JpaAttributeTypeInspection")
@Getter
@Entity
@Table(name = "option_group")
public class OptionGroup extends DomainEntity<OptionGroup, OptionGroupId> {

    @Id
    @JavaType(OptionGroupIdJavaType.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private OptionGroupId id;

    @JavaType(MenuIdJavaType.class)
    @Column(name = "menu_id")
    private MenuId menuId;

    @Column(name = "option_group_name")
    private String name;

    @Column(name = "mandatory", columnDefinition = "bit")
    private boolean mandatory;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "option", joinColumns = @JoinColumn(name = "option_group_id"))
    private List<Option> options = new ArrayList<>();

    public OptionGroup() {}

    public OptionGroup(String name, boolean mandatory, Option options) {
        this(null, name, mandatory, Collections.singletonList(options));
    }

    public OptionGroup(OptionGroupId id, String name, boolean mandatory, List<Option> options) {
        this.id = id;
        this.mandatory = mandatory;
        setName(name);
        setOptions(options);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private void setName(String name) {
        if (name == null || name.length() < 2) {
            throw new IllegalArgumentException("The option group name must be at least 2 characters.");
        }

        this.name = name;
    }

    private void setOptions(List<Option> options) {
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("There must be at least 1 option.");
        }

        this.options = options;
    }

    public Optional<Option> findOption(Option target) {
        return options.stream().filter(option -> option.equals(target)).findFirst();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isFree() {
        return options.stream().allMatch(Option::isFree);
    }

    public void chaneName(String name) {
        this.name = name;
    }

    public void changeOptionName(Option target, String optionName) {
        Option option = options.stream()
                .filter(each -> each.equals(target))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        options.remove(option);
        options.add(target.changeName(optionName));
    }
}
