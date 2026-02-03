package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a client's profile with personal information and preferences.
 * Created automatically when a user with CLIENT role registers.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "client_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientProfile extends BaseEntity {

    /** Reference to the user account */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /** Client's phone number */
    @Column(length = 20)
    private String phone;

    /** Delivery/pickup address */
    @Column(length = 255)
    private String address;

    /** Emergency contact name */
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    /** Emergency contact phone */
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    /** Additional notes or special instructions */
    @Column(columnDefinition = "TEXT")
    private String notes;
}
