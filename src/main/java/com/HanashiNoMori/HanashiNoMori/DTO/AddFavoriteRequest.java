package com.HanashiNoMori.HanashiNoMori.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFavoriteRequest {
    private Long userId;
    private Long bookId;
}
