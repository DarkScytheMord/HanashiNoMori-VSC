package com.HanashiNoMori.HanashiNoMori.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    
    @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
    private String username;
    
    @Email(message = "Email debe ser valido")
    private String email;
    
    @Size(min = 6, message = "Password debe tener minimo 6 caracteres")
    private String password;
    
    private Boolean isAdmin;
}
