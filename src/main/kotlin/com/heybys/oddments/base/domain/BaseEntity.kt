package com.heybys.oddments.base.domain

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Transient
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity : BaseEntityAware {

    @Column(length = 512, updatable = false)
    protected var createdBy: String? = null
        private set

    @Column(updatable = false)
    protected var createdDate: LocalDateTime? = null
        private set

    @Column(length = 512)
    protected var lastModifiedBy: String? = null
        private set

    @Column
    protected var lastModifiedDate: LocalDateTime? = null
        private set

    @Transient private var isCreatedByAwareMode = true

    @Transient private var isCreatedDateAwareMode = true

    @Transient private var isLastModifiedByAwareMode = true

    @Transient private var isLastModifiedDateAwareMode = true

    @PrePersist
    private fun prePersist() {
        createdBy = if (isCreatedByAwareMode) currentAuditor else createdBy
        createdDate = if (isCreatedDateAwareMode) now else createdDate
        lastModifiedBy = if (isLastModifiedByAwareMode) currentAuditor else lastModifiedBy
        lastModifiedDate = if (isLastModifiedDateAwareMode) now else lastModifiedDate
    }

    @PreUpdate
    private fun preUpdate() {
        lastModifiedBy = if (isLastModifiedByAwareMode) currentAuditor else lastModifiedBy
        lastModifiedDate = if (isLastModifiedDateAwareMode) now else lastModifiedDate
    }

    protected fun setCreatedBy(createdBy: String) {
        isCreatedByAwareMode = false
        this.createdBy = createdBy
    }

    protected fun setCreatedDate(createdDate: LocalDateTime) {
        isCreatedDateAwareMode = false
        this.createdDate = createdDate
    }

    protected fun setLastModifiedBy(lastModifiedBy: String) {
        isLastModifiedByAwareMode = false
        this.lastModifiedBy = lastModifiedBy
    }

    protected fun setLastModifiedDate(lastModifiedDate: LocalDateTime) {
        isLastModifiedDateAwareMode = false
        this.lastModifiedDate = lastModifiedDate
    }

    // Getter methods for access
    fun getCreatedByValue(): String? = createdBy

    fun getCreatedDateValue(): LocalDateTime? = createdDate

    fun getLastModifiedByValue(): String? = lastModifiedBy

    fun getLastModifiedDateValue(): LocalDateTime? = lastModifiedDate
}
