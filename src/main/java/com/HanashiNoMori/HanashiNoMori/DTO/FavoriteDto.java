package com.HanashiNoMori.HanashiNoMori.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDto {
    private Long id;
    private Long userId;
    private Long bookId;
    private BookDto book;
    private Boolean isRead;
    private String addedAt;
}
