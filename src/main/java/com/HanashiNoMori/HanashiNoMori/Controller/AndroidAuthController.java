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
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AndroidAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    /**
     * POST /api/auth/register - Registro de usuario (compatible con Android)
     * Request: { "username": "test", "email": "test@test.com", "password": "123456" }
     * Response: { "success": true, "message": "Usuario registrado exitosamente", "data": { "userId": 1, "username": "test", "email": "test@test.com" } }
     */
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<SimpleRegisterResponse> register(@Valid @RequestBody SimpleRegisterRequest request) {
        log.info("=== REGISTRO ANDROID MVP: {} ===", request.getUsername());

        try {
            // Validar si el usuario ya existe
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                log.warn("Usuario ya existe: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(SimpleRegisterResponse.error("El username ya está registrado"));
            }

            // Validar si el email ya existe
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                log.warn("Email ya existe: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(SimpleRegisterResponse.error("El email ya está registrado"));
            }

            // Crear usuario nuevo con password encriptada usando BCrypt
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword())) // ⭐ BCrypt
                    .fullName(request.getUsername()) // Usar username como fullName por defecto
                    .build();

            // Guardar usuario
            user = userRepository.saveAndFlush(user);
            log.info("✅ Usuario guardado con ID: {} - Username: {}", user.getId(), user.getUsername());

            // Asignar rol USER por defecto
            Optional<Role> userRole = roleRepository.findByName("USER");
            if (userRole.isPresent()) {
                user.getRoles().add(userRole.get());
                user = userRepository.saveAndFlush(user);
                log.info("Rol USER asignado a: {}", user.getUsername());
            }

            // ⭐⭐⭐ FORMATO CORRECTO PARA ANDROID ⭐⭐⭐
            SimpleRegisterResponse.UserData userData = SimpleRegisterResponse.UserData.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .isAdmin(user.getIsAdmin() != null ? user.getIsAdmin() : false)
                    .build();

            SimpleRegisterResponse response = SimpleRegisterResponse.success(userData);

            log.info("✅ Respuesta enviada: success={}, userId={}", response.getSuccess(), userData.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("❌ Error en registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SimpleRegisterResponse.error("Error al registrar usuario: " + e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login - Login de usuario (compatible con Android)
     * Request: { "usernameOrEmail": "test", "password": "123456" }
     * Response: { "success": true, "message": "Login exitoso", "data": { "userId": 1, "username": "test", "email": "test@test.com" } }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SimpleLoginRequest request) {
        try {
            System.out.println("=== LOGIN RECIBIDO ===");
            System.out.println("UsernameOrEmail: " + request.getUsernameOrEmail());
            System.out.println("Password: " + request.getPassword());

            // PASO 1: Buscar usuario por username O email
            Optional<User> userOpt = userRepository.findByUsernameOrEmail(
                request.getUsernameOrEmail(), 
                request.getUsernameOrEmail()
            );

            // Si no se encuentra el usuario
            if (userOpt.isEmpty()) {
                System.out.println("❌ ERROR: Usuario no encontrado");
                
                SimpleLoginResponse errorResponse = SimpleLoginResponse.builder()
                    .success(false)
                    .message("Usuario no encontrado")
                    .data(null)
                    .build();
                
                return ResponseEntity.status(401).body(errorResponse);
            }

            User user = userOpt.get();
            System.out.println("✅ Usuario encontrado: " + user.getUsername() + " (ID: " + user.getId() + ")");

            // PASO 2: Verificar password usando BCrypt
            boolean passwordMatch = passwordEncoder.matches(
                request.getPassword(),  // Password en texto plano (123456)
                user.getPassword()      // Password hasheada de la BD ($2a$10$...)
            );

            System.out.println("Password match: " + passwordMatch);

            if (!passwordMatch) {
                System.out.println("❌ ERROR: Contraseña incorrecta");
                
                SimpleLoginResponse errorResponse = SimpleLoginResponse.builder()
                    .success(false)
                    .message("Contraseña incorrecta")
                    .data(null)
                    .build();
                
                return ResponseEntity.status(401).body(errorResponse);
            }

            System.out.println("✅ LOGIN EXITOSO");

            // PASO 3: Crear respuesta exitosa
            SimpleLoginResponse.UserData userData = SimpleLoginResponse.UserData.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isAdmin(user.getIsAdmin() != null ? user.getIsAdmin() : false)
                .build();

            SimpleLoginResponse response = SimpleLoginResponse.builder()
                .success(true)
                .message("Login exitoso")
                .data(userData)
                .build();

            System.out.println("✅ Enviando respuesta: " + response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ EXCEPCIÓN: " + e.getMessage());
            e.printStackTrace();
            
            SimpleLoginResponse errorResponse = SimpleLoginResponse.builder()
                .success(false)
                .message("Error al hacer login: " + e.getMessage())
                .data(null)
                .build();
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Endpoint de prueba para verificar que el backend está corriendo
     * GET /api/auth/ping
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Backend Android MVP funcionando correctamente ✅");
    }
    
    /**
     * Endpoint temporal para generar hash BCrypt (SOLO DESARROLLO)
     * GET /api/auth/hash?password=admin123
     */
    @GetMapping("/hash")
    public ResponseEntity<String> generateHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        return ResponseEntity.ok("Password: " + password + "\nHash BCrypt: " + hash);
    }
}
