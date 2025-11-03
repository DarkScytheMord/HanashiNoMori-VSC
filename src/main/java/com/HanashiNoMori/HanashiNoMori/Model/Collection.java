package com.HanashiNoMori.HanashiNoMori.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "collections", indexes = {
    @Index(name = "idx_user_collections", columnList = "user_id"),
    @Index(name = "idx_public_collections", columnList = "is_public")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_public")
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(name = "items_count")
    @Builder.Default
    private Integer itemsCount = 0;

    @ManyToMany
    @JoinTable(
        name = "collection_media",
        joinColumns = @JoinColumn(name = "collection_id"),
        inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    @Builder.Default
    private Set<Media> mediaItems = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
