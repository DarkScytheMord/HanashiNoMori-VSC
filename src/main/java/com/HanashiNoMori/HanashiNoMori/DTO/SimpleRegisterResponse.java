package com.HanashiNoMori.HanashiNoMori.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta simplificada para registro - Compatible con app Android MVP
 * Devuelve: success, message, userId
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRegisterResponse {
    
    private Boolean success;
    private String message;
    private Integer userId;
    
    public static SimpleRegisterResponse success(Integer userId) {
        return new SimpleRegisterResponse(true, "Usuario registrado exitosamente", userId);
    }
    
    public static SimpleRegisterResponse error(String message) {
        return new SimpleRegisterResponse(false, message, null);
    }
}
