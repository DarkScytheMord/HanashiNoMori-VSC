package com.HanashiNoMori.HanashiNoMori.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta simplificada para registro - Compatible con app Android MVP
 * Devuelve: success, message, data { userId, username, email }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRegisterResponse {
    
    private Boolean success;
    private String message;
    private UserData data;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        private Long userId;
        private String username;
        private String email;
        private Boolean isAdmin;
    }
    
    public static SimpleRegisterResponse success(UserData userData) {
        return SimpleRegisterResponse.builder()
                .success(true)
                .message("Usuario registrado exitosamente")
                .data(userData)
                .build();
    }
    
    public static SimpleRegisterResponse error(String message) {
        return SimpleRegisterResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
