package com.heybys.oddments.fooddelivery.domain.shop

import com.heybys.oddments.base.domain.DomainEntity
import com.heybys.oddments.fooddelivery.domain.shop.MenuId.MenuIdJavaType
import com.heybys.oddments.fooddelivery.domain.shop.OptionGroupId.OptionGroupIdJavaType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import org.hibernate.annotations.JavaType
import java.util.Collections
import java.util.Optional

@Suppress("JpaAttributeTypeInspection")
@Entity
@Table(name = "option_group")
class OptionGroup : DomainEntity<OptionGroup, OptionGroupId> {

    @Id
    @JavaType(OptionGroupIdJavaType::class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: OptionGroupId? = null

    override fun getId(): OptionGroupId? = _id

    @JavaType(MenuIdJavaType::class)
    @Column(name = "menu_id")
    var menuId: MenuId? = null

    @Column(name = "option_group_name")
    var name: String? = null
        private set

    @Column(name = "mandatory", columnDefinition = "bit")
    var isMandatory: Boolean = false

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "options", joinColumns = [JoinColumn(name = "option_group_id")])
    var options: MutableList<Option> = mutableListOf()
        private set

    constructor()

    constructor(
        name: String,
        mandatory: Boolean,
        option: Option,
    ) : this(null, name, mandatory, Collections.singletonList(option))

    constructor(id: OptionGroupId?, name: String, mandatory: Boolean, options: List<Option>) {
        this._id = id
        this.isMandatory = mandatory
        setName(name)
        setOptions(options.toMutableList())
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()

    private fun setName(name: String) {
        if (name.length < 2) {
            throw IllegalArgumentException("The option group name must be at least 2 characters.")
        }
        this.name = name
    }

    private fun setOptions(options: MutableList<Option>) {
        if (options.isEmpty()) {
            throw IllegalArgumentException("There must be at least 1 option.")
        }
        this.options = options
    }

    fun findOption(target: Option): Optional<Option> =
        options.stream().filter { option -> option == target }.findFirst()

    fun isFree(): Boolean = options.all { it.isFree() }

    fun chaneName(name: String) {
        this.name = name
    }

    fun changeOptionName(target: Option, optionName: String) {
        val option =
            options
                .stream()
                .filter { it == target }
                .findFirst()
                .orElseThrow { IllegalArgumentException() }

        options.remove(option)
        options.add(target.changeName(optionName))
    }
}
