package com.HanashiNoMori.HanashiNoMori.Service;

import com.HanashiNoMori.HanashiNoMori.DTO.AuthResponse;
import com.HanashiNoMori.HanashiNoMori.DTO.LoginRequest;
import com.HanashiNoMori.HanashiNoMori.DTO.RegisterRequest;
import com.HanashiNoMori.HanashiNoMori.Model.Role;
import com.HanashiNoMori.HanashiNoMori.Model.User;
import com.HanashiNoMori.HanashiNoMori.Repository.RoleRepository;
import com.HanashiNoMori.HanashiNoMori.Repository.UserRepository;
import com.HanashiNoMori.HanashiNoMori.Security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de autenticación SIMPLIFICADO
 * - Sin encriptación de contraseñas (texto plano)
 * - Sin Spring Security
 * - Para proyectos universitarios/demos
 */
@Service
public class SimpleAuthService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleAuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * Registro simple: contraseña en texto plano
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("=== REGISTRO SIMPLE: {} ===", request.getUsername());

        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Buscar o crear rol USER
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("USER");
                    newRole.setDescription("Usuario estándar");
                    newRole.setUsers(new HashSet<>());
                    return roleRepository.save(newRole);
                });

        // Crear usuario con contraseña en texto plano
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // ⚠️ SIN ENCRIPTAR
        user.setFullName(request.getFullName());
        user.setIsActive(true);
        user.setEmailVerified(false);
        user.setRoles(new HashSet<>());

        // Guardar usuario
        user = userRepository.saveAndFlush(user);
        logger.info("Usuario guardado: ID={}", user.getId());

        // Asignar rol
        user.getRoles().add(userRole);
        user = userRepository.saveAndFlush(user);
        logger.info("Rol asignado correctamente");

        // Generar tokens
        String token = tokenProvider.generateToken(user.getUsername());
        String refreshToken = tokenProvider.generateRefreshToken(user.getUsername());

        // Obtener nombres de roles
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        logger.info("=== REGISTRO EXITOSO: {} ===", user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roleNames)
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * Login simple: comparación directa de contraseñas
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        logger.info("=== LOGIN SIMPLE: {} ===", request.getUsername());

        // Buscar usuario
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));

        // Comparar contraseñas en texto plano
        if (!user.getPassword().equals(request.getPassword())) {
            logger.warn("Contraseña incorrecta para usuario: {}", request.getUsername());
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        // Verificar si el usuario está activo
        if (!user.getIsActive()) {
            throw new RuntimeException("La cuenta está desactivada");
        }

        logger.info("Contraseña correcta, generando tokens...");

        // Generar tokens
        String token = tokenProvider.generateToken(user.getUsername());
        String refreshToken = tokenProvider.generateRefreshToken(user.getUsername());

        // Obtener nombres de roles
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        logger.info("=== LOGIN EXITOSO: {} ===", user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * Refresh token (mantener funcionalidad)
     */
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String newToken = tokenProvider.generateToken(username);
        String newRefreshToken = tokenProvider.generateRefreshToken(username);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
