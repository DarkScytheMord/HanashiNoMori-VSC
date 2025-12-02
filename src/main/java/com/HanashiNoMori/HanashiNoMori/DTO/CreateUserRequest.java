package com.HanashiNoMori.HanashiNoMori.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    
    @NotBlank(message = "Username es obligatorio")
    @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
    private String username;
    
    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Email debe ser valido")
    private String email;
    
    @NotBlank(message = "Password es obligatorio")
    @Size(min = 6, message = "Password debe tener minimo 6 caracteres")
    private String password;
    
    @Builder.Default
    private Boolean isAdmin = false;
}
