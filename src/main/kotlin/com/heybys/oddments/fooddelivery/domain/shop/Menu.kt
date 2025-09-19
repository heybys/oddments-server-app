package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.AggregateRoot
import com.heybys.oddments.fooddelivery.domain.shop.MenuId.MenuIdJavaType
import com.heybys.oddments.fooddelivery.domain.shop.ShopId.ShopIdJavaType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.JavaType
import java.util.Optional

@Suppress("JpaAttributeTypeInspection")
@Entity
@Table(name = "menu")
class Menu : AggregateRoot<Menu, MenuId> {

    @Id
    @JavaType(MenuIdJavaType::class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: MenuId? = null

    override fun getId(): MenuId? = _id

    @JavaType(ShopIdJavaType::class)
    @Column(name = "shop_id")
    var shopId: ShopId? = null

    @Column(name = "menu_name")
    var name: String? = null

    @Column(name = "menu_description")
    var description: String? = null

    @Column(name = "open", columnDefinition = "bit")
    var isOpen: Boolean = false

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    var groups: MutableList<OptionGroup> = mutableListOf()

    constructor()

    constructor(
        shopId: ShopId,
        name: String,
        description: String,
    ) : this(null, shopId, name, description, false, mutableListOf())

    constructor(
        id: MenuId?,
        shopId: ShopId,
        name: String,
        description: String,
        open: Boolean,
        groups: MutableList<OptionGroup>,
    ) {
        this._id = id
        this.shopId = shopId
        this.name = name
        this.description = description
        this.isOpen = open
        this.groups = groups
    }

    override fun hashCode(): Int = super.hashCode()

    override fun equals(other: Any?): Boolean = super.equals(other)

    fun addOptionGroup(optionGroup: OptionGroup?) {
        requireNotNull(optionGroup) { "OptionGroup cannot be null." }

        if (groups.any { it.name == optionGroup.name }) {
            throw IllegalArgumentException("An option group with the same name already exists.")
        }

        if (isOpen && optionGroup.isMandatory && countOfMandatoryOptionGroups() >= 3) {
            throw IllegalArgumentException("Up to three required option groups can be registered.")
        }

        groups.add(optionGroup)
    }

    fun open() {
        if (groups.isEmpty()) {
            throw IllegalStateException("There must be at least one option group.")
        }

        if (countOfMandatoryOptionGroups() == 0L) {
            throw IllegalStateException("At least one required option group must be registered.")
        }

        if (countOfMandatoryOptionGroups() > 3) {
            throw IllegalStateException("Only up to 3 required option groups can be registered.")
        }

        if (countOfFreeOptionGroups() < 1) {
            throw IllegalStateException("At least one option group with a set amount must be registered.")
        }

        isOpen = true
    }

    fun removeOptionGroup(optionGroupId: OptionGroupId?) {
        requireNotNull(optionGroupId) { "Parameters must not be null." }

        if (groups.isEmpty()) {
            throw IllegalArgumentException("The option groups is empty.")
        }

        val optionGroup =
            groups.firstOrNull { it.getId() == optionGroupId }
                ?: throw IllegalArgumentException("Option group not found.")

        if (!isOpen) {
            groups.remove(optionGroup)
            return
        }

        if (groups.size == 1) {
            throw IllegalArgumentException("At least one option group must be registered.")
        }

        if (optionGroup.isMandatory && countOfMandatoryOptionGroups() == 1L) {
            throw IllegalArgumentException("At least one required option group must be registered.")
        }

        if (!optionGroup.isFree() && countOfFreeOptionGroups() == 1L) {
            throw IllegalArgumentException(
                "At least one option group with a set amount must be registered.",
            )
        }

        groups.remove(optionGroup)
    }

    fun changeOptionGroupName(optionGroupId: OptionGroupId?, name: String?) {
        requireNotNull(optionGroupId) { "Parameters must not be null." }

        if (groups.isEmpty()) {
            throw IllegalArgumentException("The option groups is empty.")
        }

        if (groups.any { it.name == name }) {
            throw IllegalArgumentException("An option group with the same name already exists.")
        }

        val optionGroup =
            getOptionGroup(optionGroupId).orElseThrow {
                IllegalArgumentException("Option group not found.")
            }

        optionGroup.chaneName(name!!)
    }

    fun changeOptionName(optionGroupId: OptionGroupId?, target: Option?, optionName: String?) {
        requireNotNull(optionGroupId) { "The optionGroupId parameter must not be null." }
        requireNotNull(target) { "The target Option parameter must not be null." }

        if (groups.isEmpty()) {
            throw IllegalArgumentException("The option groups is empty.")
        }

        val optionGroup =
            getOptionGroup(optionGroupId).orElseThrow {
                IllegalArgumentException("Option group not found.")
            }

        optionGroup.changeOptionName(target, optionName!!)
    }

    private fun countOfMandatoryOptionGroups(): Long = groups.count { it.isMandatory }.toLong()

    private fun countOfFreeOptionGroups(): Long = groups.count { !it.isFree() }.toLong()

    private fun getOptionGroup(optionGroupId: OptionGroupId): Optional<OptionGroup> =
        groups.stream().filter { it.getId() == optionGroupId }.findFirst()

    fun getOptionGroup(name: String): Optional<OptionGroup> =
        groups.stream().filter { it.name == name }.findFirst()
}
