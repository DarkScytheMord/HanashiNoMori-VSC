package com.HanashiNoMori.HanashiNoMori.Controller;

import com.HanashiNoMori.HanashiNoMori.DTO.ApiResponse;
import com.HanashiNoMori.HanashiNoMori.DTO.AuthResponse;
import com.HanashiNoMori.HanashiNoMori.DTO.LoginRequest;
import com.HanashiNoMori.HanashiNoMori.DTO.RegisterRequest;
import com.HanashiNoMori.HanashiNoMori.Service.SimpleAuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación SIMPLIFICADO
 * Sin Spring Security para facilitar proyectos universitarios
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private SimpleAuthService authService;

    /**
     * Registro de nuevo usuario
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            logger.info("Petición de registro recibida para usuario: {}", request.getUsername());
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Usuario registrado exitosamente", response));
        } catch (Exception e) {
            logger.error("Error en registro: {}", e.getMessage(), e);
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Error desconocido en el registro";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(errorMessage));
        }
    }

    /**
     * Login de usuario
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Petición de login recibida para usuario: {}", request.getUsername());
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
        } catch (Exception e) {
            logger.error("Error en login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Credenciales inválidas"));
        }
    }

    /**
     * Refrescar token JWT
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            // Remover "Bearer " del token
            String token = refreshToken.replace("Bearer ", "");
            logger.info("Petición de refresh token recibida");
            AuthResponse response = authService.refreshToken(token);
            return ResponseEntity.ok(ApiResponse.success("Token refrescado exitosamente", response));
        } catch (Exception e) {
            logger.error("Error al refrescar token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Refresh token inválido o expirado"));
        }
    }

    /**
     * Logout de usuario
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        logger.info("Logout solicitado");
        return ResponseEntity.ok(ApiResponse.success("Sesión cerrada exitosamente", null));
    }
}
