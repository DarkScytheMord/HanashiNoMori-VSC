package com.HanashiNoMori.HanashiNoMori.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simplificado para login - Compatible con app Android MVP
 * Solo requiere: username, password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleLoginRequest {
    
    @NotBlank(message = "El username es obligatorio")
    private String username;
    
    @NotBlank(message = "El password es obligatorio")
    private String password;
}
