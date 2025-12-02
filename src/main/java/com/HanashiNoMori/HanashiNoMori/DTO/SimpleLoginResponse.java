package com.HanashiNoMori.HanashiNoMori.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta simplificada para login - Compatible con app Android MVP
 * Devuelve: success, message, data { userId, username, email }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleLoginResponse {
    
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
    }
    
    public static SimpleLoginResponse success(UserData userData) {
        return SimpleLoginResponse.builder()
                .success(true)
                .message("Login exitoso")
                .data(userData)
                .build();
    }
    
    public static SimpleLoginResponse error(String message) {
        return SimpleLoginResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
