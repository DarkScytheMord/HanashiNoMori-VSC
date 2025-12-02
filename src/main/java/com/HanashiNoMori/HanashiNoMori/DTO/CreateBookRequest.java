package com.HanashiNoMori.HanashiNoMori.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookRequest {
    
    @NotBlank(message = "Title es obligatorio")
    private String title;
    
    @NotBlank(message = "Author es obligatorio")
    private String author;
    
    @NotBlank(message = "Category es obligatorio")
    private String category;
    
    private String description;
    private String coverUrl;
    private String isbn;
}
