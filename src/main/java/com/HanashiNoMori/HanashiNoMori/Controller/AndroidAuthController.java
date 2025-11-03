package com.HanashiNoMori.HanashiNoMori.Controller;

import com.HanashiNoMori.HanashiNoMori.DTO.SimpleLoginRequest;
import com.HanashiNoMori.HanashiNoMori.DTO.SimpleLoginResponse;
import com.HanashiNoMori.HanashiNoMori.DTO.SimpleRegisterRequest;
import com.HanashiNoMori.HanashiNoMori.DTO.SimpleRegisterResponse;
import com.HanashiNoMori.HanashiNoMori.Model.Role;
import com.HanashiNoMori.HanashiNoMori.Model.User;
import com.HanashiNoMori.HanashiNoMori.Repository.RoleRepository;
import com.HanashiNoMori.HanashiNoMori.Repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller simplificado para Android App (MVP)
 * Endpoints que coinciden EXACTAMENTE con la API que espera la app Android
 */
@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class AndroidAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * POST /users - Registro de usuario (compatible con Android)
     * Request: { "username": "test", "email": "test@test.com", "password": "123456" }
     * Response: { "success": true, "message": "Usuario registrado exitosamente", "userId": 1 }
     */
    @PostMapping
    @Transactional
    public ResponseEntity<SimpleRegisterResponse> register(@Valid @RequestBody SimpleRegisterRequest request) {
        log.info("=== REGISTRO ANDROID MVP: {} ===", request.getUsername());

        try {
            // Validar si el usuario ya existe
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                log.warn("Usuario ya existe: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(SimpleRegisterResponse.error("El usuario ya existe"));
            }

            // Validar si el email ya existe
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                log.warn("Email ya existe: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(SimpleRegisterResponse.error("El email ya está registrado"));
            }

            // Crear usuario nuevo - campos sin especificar usan @Builder.Default de la entidad
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(request.getPassword()) // ⚠️ Texto plano para MVP universitario
                    .fullName(request.getUsername()) // Usar username como fullName por defecto
                    .build();

            // Guardar usuario
            user = userRepository.saveAndFlush(user);
            log.info("Usuario guardado con ID: {} - fullName: {}", user.getId(), user.getFullName());

            // Asignar rol USER por defecto
            Optional<Role> userRole = roleRepository.findByName("USER");
            if (userRole.isPresent()) {
                user.getRoles().add(userRole.get());
                user = userRepository.saveAndFlush(user);
                log.info("Rol USER asignado a: {}", user.getUsername());
            }

            // Respuesta simplificada compatible con Android
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SimpleRegisterResponse.success(user.getId().intValue()));

        } catch (Exception e) {
            log.error("Error en registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SimpleRegisterResponse.error("Error al registrar usuario: " + e.getMessage()));
        }
    }

    /**
     * POST /users/login - Login de usuario (compatible con Android)
     * Request: { "username": "test", "password": "123456" }
     * Response: { "success": true, "message": "Login exitoso", "userId": 1, "username": "test" }
     */
    @PostMapping("/login")
    @Transactional(readOnly = true)
    public ResponseEntity<SimpleLoginResponse> login(@Valid @RequestBody SimpleLoginRequest request) {
        log.info("=== LOGIN ANDROID MVP: {} ===", request.getUsername());

        try {
            // Buscar usuario
            Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

            if (userOpt.isEmpty()) {
                log.warn("Usuario no encontrado: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(SimpleLoginResponse.error("Usuario o contraseña incorrectos"));
            }

            User user = userOpt.get();

            // Comparar contraseña en texto plano (MVP universitario)
            if (!user.getPassword().equals(request.getPassword())) {
                log.warn("Contraseña incorrecta para: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(SimpleLoginResponse.error("Usuario o contraseña incorrectos"));
            }

            log.info("Login exitoso para: {}", user.getUsername());

            // Respuesta simplificada compatible con Android
            return ResponseEntity.ok(
                    SimpleLoginResponse.success(user.getId().intValue(), user.getUsername())
            );

        } catch (Exception e) {
            log.error("Error en login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SimpleLoginResponse.error("Error en el servidor: " + e.getMessage()));
        }
    }

    /**
     * Endpoint de prueba para verificar que el backend está corriendo
     * GET /users/ping
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Backend Android MVP funcionando correctamente ✅");
    }
}
