package com.heybys.oddments.base.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;

import lombok.Getter;

@Getter
@MappedSuperclass
abstract class BaseEntity implements BaseEntityAware {

    @Column(length = 512, updatable = false)
    private String createdBy;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(length = 512)
    private String lastModifiedBy;

    @Column
    private LocalDateTime lastModifiedDate;

    @Transient
    private boolean isCreatedByAwareMode = true;

    @Transient
    private boolean isCreatedDateAwareMode = true;

    @Transient
    private boolean isLastModifiedByAwareMode = true;

    @Transient
    private boolean isLastModifiedDateAwareMode = true;

    @PrePersist
    private void prePersist() {
        createdBy = isCreatedByAwareMode ? getCurrentAuditor() : createdBy;
        createdDate = isCreatedDateAwareMode ? getNow() : createdDate;
        lastModifiedBy = isLastModifiedByAwareMode ? getCurrentAuditor() : lastModifiedBy;
        lastModifiedDate = isLastModifiedDateAwareMode ? getNow() : lastModifiedDate;
    }

    @PreUpdate
    private void preUpdate() {
        lastModifiedBy = isLastModifiedByAwareMode ? getCurrentAuditor() : lastModifiedBy;
        lastModifiedDate = isLastModifiedDateAwareMode ? getNow() : lastModifiedDate;
    }

    protected void setCreatedBy(String createdBy) {
        isCreatedByAwareMode = false;
        this.createdBy = createdBy;
    }

    protected void setCreatedDate(LocalDateTime createdDate) {
        isCreatedDateAwareMode = false;
        this.createdDate = createdDate;
    }

    protected void setLastModifiedBy(String lastModifiedBy) {
        isLastModifiedByAwareMode = false;
        this.lastModifiedBy = lastModifiedBy;
    }

    protected void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        isLastModifiedDateAwareMode = false;
        this.lastModifiedDate = lastModifiedDate;
    }
}
