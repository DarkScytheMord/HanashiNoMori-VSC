package com.HanashiNoMori.HanashiNoMori.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String icon;

    @Column(length = 255)
    private String description;

    @Column(name = "color_hex", length = 7)
    private String colorHex;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Media> mediaList = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
