package com.HanashiNoMori.HanashiNoMori.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String refreshToken;
    
    @Builder.Default
    private String tokenType = "Bearer";
    
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private LocalDateTime createdAt;
}
