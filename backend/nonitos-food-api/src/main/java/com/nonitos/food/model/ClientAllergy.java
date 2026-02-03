package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Junction table linking clients to their allergies.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "client_allergies", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"client_profile_id", "allergy_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientAllergy extends BaseEntity {

    /** Reference to the client profile */
    @ManyToOne
    @JoinColumn(name = "client_profile_id", nullable = false)
    private ClientProfile clientProfile;

    /** Reference to the allergy */
    @ManyToOne
    @JoinColumn(name = "allergy_id", nullable = false)
    private Allergy allergy;

    /** Severity level (MILD, MODERATE, SEVERE) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AllergySeverity severity;

    /** Additional notes about this specific allergy */
    @Column(length = 255)
    private String notes;

    public enum AllergySeverity {
        MILD, MODERATE, SEVERE
    }
}
