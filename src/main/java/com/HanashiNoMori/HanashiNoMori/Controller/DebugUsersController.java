package com.HanashiNoMori.HanashiNoMori.Controller;

import com.HanashiNoMori.HanashiNoMori.Model.User;
import com.HanashiNoMori.HanashiNoMori.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller para debug y verificación de usuarios
 */
@Slf4j
@RestController
@RequestMapping("/users/debug")
@CrossOrigin(origins = "*")
public class DebugUsersController {

    @Autowired
    private UserRepository userRepository;

    /**
     * GET /users/debug/list - Lista todos los usuarios registrados
     */
    @GetMapping("/list")
    public ResponseEntity<?> listAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            
            List<Map<String, Object>> usersList = users.stream().map(user -> {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("username", user.getUsername());
                userData.put("email", user.getEmail());
                userData.put("password", user.getPassword()); // Texto plano para debug
                userData.put("fullName", user.getFullName());
                userData.put("createdAt", user.getCreatedAt());
                userData.put("rolesCount", user.getRoles() != null ? user.getRoles().size() : 0);
                return userData;
            }).collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalUsers", users.size());
            response.put("users", usersList);
            
            log.info("Listando {} usuarios registrados", users.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error al listar usuarios: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * GET /users/debug/count - Cuenta total de usuarios
     */
    @GetMapping("/count")
    public ResponseEntity<?> countUsers() {
        try {
            long count = userRepository.count();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalUsers", count);
            response.put("message", "Total de usuarios en la base de datos");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error al contar usuarios: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * GET /users/debug/last/{n} - Obtiene los últimos N usuarios registrados
     */
    @GetMapping("/last/{count}")
    public ResponseEntity<?> getLastUsers(@PathVariable int count) {
        try {
            List<User> allUsers = userRepository.findAll();
            
            // Ordenar por ID descendente y tomar los últimos N
            List<User> lastUsers = allUsers.stream()
                    .sorted((a, b) -> b.getId().compareTo(a.getId()))
                    .limit(count)
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> usersList = lastUsers.stream().map(user -> {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("username", user.getUsername());
                userData.put("email", user.getEmail());
                userData.put("password", user.getPassword());
                userData.put("fullName", user.getFullName());
                userData.put("createdAt", user.getCreatedAt());
                return userData;
            }).collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", lastUsers.size());
            response.put("users", usersList);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error al obtener últimos usuarios: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
