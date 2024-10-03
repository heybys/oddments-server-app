package com.heybys.oddments.base.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

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

    @Column()
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        createdBy = getCurrentAuditor();
        createdDate = getNow();

        lastModifiedBy = getCurrentAuditor();
        lastModifiedDate = getNow();
    }

    @PreUpdate
    private void preUpdate() {
        lastModifiedBy = getCurrentAuditor();
        lastModifiedDate = getNow();
    }
}
