package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Junction table linking clients to their dietary restrictions.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "client_restrictions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"client_profile_id", "restriction_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRestriction extends BaseEntity {

    /** Reference to the client profile */
    @ManyToOne
    @JoinColumn(name = "client_profile_id", nullable = false)
    private ClientProfile clientProfile;

    /** Reference to the dietary restriction */
    @ManyToOne
    @JoinColumn(name = "restriction_id", nullable = false)
    private DietaryRestriction restriction;

    /** Additional notes about this specific restriction */
    @Column(length = 255)
    private String notes;
}
