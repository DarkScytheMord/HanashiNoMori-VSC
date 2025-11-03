package com.HanashiNoMori.HanashiNoMori.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta simplificada para login - Compatible con app Android MVP
 * Devuelve: success, message, userId, username
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleLoginResponse {
    
    private Boolean success;
    private String message;
    private Integer userId;
    private String username;
    
    public static SimpleLoginResponse success(Integer userId, String username) {
        return new SimpleLoginResponse(true, "Login exitoso", userId, username);
    }
    
    public static SimpleLoginResponse error(String message) {
        return new SimpleLoginResponse(false, message, null, null);
    }
}
