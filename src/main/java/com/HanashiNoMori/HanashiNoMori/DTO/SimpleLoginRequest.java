package com.HanashiNoMori.HanashiNoMori.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simplificado para login - Compatible con app Android MVP
 * Solo requiere: usernameOrEmail, password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleLoginRequest {
    
    @NotBlank(message = "El username o email es obligatorio")
    private String usernameOrEmail;
    
    @NotBlank(message = "El password es obligatorio")
    private String password;
}
