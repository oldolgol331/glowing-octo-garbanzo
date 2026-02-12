package com.example.demo.common.model

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * PackageName : com.example.demo.common.model
 * FileName    : BaseAuditingEntity
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description : 도메인 모델 공통 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditingEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt = LocalDateTime.now()
        protected set

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt = LocalDateTime.now()
        protected set

}