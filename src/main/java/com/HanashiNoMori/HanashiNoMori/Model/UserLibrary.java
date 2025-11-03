package com.HanashiNoMori.HanashiNoMori.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_library", 
    uniqueConstraints = @UniqueConstraint(name = "unique_user_media", columnNames = {"user_id", "media_id"}),
    indexes = {
        @Index(name = "idx_user", columnList = "user_id"),
        @Index(name = "idx_favorite", columnList = "is_favorite")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "is_favorite")
    @Builder.Default
    private Boolean isFavorite = false;

    @Column
    private Integer rating;

    @CreationTimestamp
    @Column(name = "added_date", nullable = false, updatable = false)
    private LocalDateTime addedDate;

    @Column(name = "last_read_date")
    private LocalDateTime lastReadDate;
}
