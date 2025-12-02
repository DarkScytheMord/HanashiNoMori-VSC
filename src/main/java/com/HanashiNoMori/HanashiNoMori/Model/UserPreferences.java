package com.HanashiNoMori.HanashiNoMori.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(length = 20)
    @Builder.Default
    private String theme = "light";

    @Column(nullable = false)
    @Builder.Default
    private Boolean notificationsEnabled = true;

    @Column(length = 50)
    private String favoriteCategory;

    @Column(length = 10)
    @Builder.Default
    private String language = "es";

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
