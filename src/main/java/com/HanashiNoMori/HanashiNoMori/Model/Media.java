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
@Table(name = "media", indexes = {
    @Index(name = "idx_category", columnList = "category_id"),
    @Index(name = "idx_title", columnList = "title"),
    @Index(name = "idx_author", columnList = "author")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "total_chapters")
    private Integer totalChapters;

    @Column(length = 50)
    @Builder.Default
    private String status = "EN_CURSO";

    @Column(name = "is_custom")
    @Builder.Default
    private Boolean isCustom = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToMany
    @JoinTable(
        name = "media_tags",
        joinColumns = @JoinColumn(name = "media_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserLibrary> userLibraries = new HashSet<>();

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
