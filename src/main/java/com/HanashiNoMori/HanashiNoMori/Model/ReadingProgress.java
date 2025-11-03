package com.HanashiNoMori.HanashiNoMori.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reading_progress",
    uniqueConstraints = @UniqueConstraint(name = "unique_user_media_progress", columnNames = {"user_id", "media_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    @Column(name = "current_chapter")
    @Builder.Default
    private Integer currentChapter = 0;

    @Column(name = "current_page")
    @Builder.Default
    private Integer currentPage = 0;

    @Column(name = "percentage_completed", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal percentageCompleted = BigDecimal.ZERO;

    @Column(name = "time_spent_minutes")
    @Builder.Default
    private Integer timeSpentMinutes = 0;

    @UpdateTimestamp
    @Column(name = "last_read_date")
    private LocalDateTime lastReadDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
