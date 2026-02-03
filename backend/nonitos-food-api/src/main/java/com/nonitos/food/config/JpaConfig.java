package com.nonitos.food.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration for the application.
 * 
 * <p>Enables JPA auditing to automatically populate audit fields (createdAt,
 * updatedAt) in entities that extend BaseEntity.</p>
 * 
 * <h2>What is JPA Auditing?</h2>
 * <p>JPA Auditing automatically sets timestamp fields when entities are created
 * or modified. Without this, we'd need to manually set these fields in every
 * service method, which is error-prone and repetitive.</p>
 * 
 * <h2>How it works:</h2>
 * <ul>
 *   <li>@CreatedDate fields are set once when entity is first persisted</li>
 *   <li>@LastModifiedDate fields are updated on every save operation</li>
 *   <li>Requires @EntityListeners(AuditingEntityListener.class) on entity</li>
 * </ul>
 *
 * @author Nonito's Food Team
 * @since 1.0
 * @see com.nonitos.food.model.BaseEntity for audit fields
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
