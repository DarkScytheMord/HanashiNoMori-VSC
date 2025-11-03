package com.HanashiNoMori.HanashiNoMori.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simplificado para registro - Compatible con app Android MVP
 * Solo requiere: username, email, password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRegisterRequest {
    
    @NotBlank(message = "El username es obligatorio")
    private String username;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotBlank(message = "El password es obligatorio")
    private String password;
}
