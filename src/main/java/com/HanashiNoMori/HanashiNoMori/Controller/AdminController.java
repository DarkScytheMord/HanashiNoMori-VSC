package com.HanashiNoMori.HanashiNoMori.Controller;

import com.HanashiNoMori.HanashiNoMori.DTO.*;
import com.HanashiNoMori.HanashiNoMori.Model.User;
import com.HanashiNoMori.HanashiNoMori.Repository.UserRepository;
import com.HanashiNoMori.HanashiNoMori.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para administracion del sistema
 * Todos los endpoints requieren que el usuario sea administrador (isAdmin = true)
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;

    // ============================================
    // MIDDLEWARE - VERIFICACION DE ADMIN
    // ============================================

    /**
     * Verifica que el usuario tenga permisos de administrador
     * En un sistema real, esto seria un interceptor o filtro
     * Para este MVP, usamos un header "X-User-Id" para identificar al usuario
     */
    private void verifyAdmin(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User ID no proporcionado");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getIsAdmin() == null || !user.getIsAdmin()) {
            throw new RuntimeException("Acceso denegado: se requieren permisos de administrador");
        }
    }

    // ============================================
    // ENDPOINTS DE USUARIOS
    // ============================================

    /**
     * GET /api/admin/users?adminUserId=1
     * Obtener todos los usuarios
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers(
            @RequestParam(value = "adminUserId", required = false) Long userId) {
        
        try {
            verifyAdmin(userId);
            
            List<UserDto> users = adminService.getAllUsers();
            log.info("Usuarios obtenidos: {}", users.size());
            
            return ResponseEntity.ok(ApiResponse.success("Usuarios obtenidos", users));
            
        } catch (RuntimeException e) {
            log.error("Error al obtener usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/admin/users/{userId}?adminUserId=1
     * Obtener un usuario por ID
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(
            @PathVariable Long userId,
            @RequestParam(value = "adminUserId", required = false) Long adminUserId) {
        
        try {
            verifyAdmin(adminUserId);
            
            UserDto user = adminService.getUserById(userId);
            return ResponseEntity.ok(ApiResponse.success("Usuario obtenido", user));
            
        } catch (RuntimeException e) {
            log.error("Error al obtener usuario: {}", e.getMessage());
            
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/admin/users?adminUserId=1
     * Crear nuevo usuario
     */
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserDto>> createUser(
            @Valid @RequestBody CreateUserRequest request,
            @RequestParam(value = "adminUserId", required = false) Long adminUserId) {
        
        try {
            verifyAdmin(adminUserId);
            
            UserDto newUser = adminService.createUser(request);
            log.info("Usuario creado: {}", newUser.getUsername());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Usuario creado exitosamente", newUser));
            
        } catch (RuntimeException e) {
            log.error("Error al crear usuario: {}", e.getMessage());
            
            if (e.getMessage().contains("Acceso denegado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PUT /api/admin/users/{userId}?adminUserId=1
     * Actualizar usuario
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request,
            @RequestParam(value = "adminUserId", required = false) Long adminUserId) {
        
        try {
            verifyAdmin(adminUserId);
            
            UserDto updatedUser = adminService.updateUser(userId, request);
            log.info("Usuario actualizado: {}", updatedUser.getUsername());
            
            return ResponseEntity.ok(ApiResponse.success("Usuario actualizado", updatedUser));
            
        } catch (RuntimeException e) {
            log.error("Error al actualizar usuario: {}", e.getMessage());
            
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            if (e.getMessage().contains("Acceso denegado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/admin/users/{userId}?adminUserId=1
     * Eliminar usuario
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long userId,
            @RequestParam(value = "adminUserId", required = false) Long adminUserId) {
        
        try {
            verifyAdmin(adminUserId);
            
            adminService.deleteUser(userId);
            log.info("Usuario eliminado: ID={}", userId);
            
            return ResponseEntity.ok(ApiResponse.success("Usuario eliminado exitosamente", null));
            
        } catch (RuntimeException e) {
            log.error("Error al eliminar usuario: {}", e.getMessage());
            
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            if (e.getMessage().contains("ultimo administrador")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ============================================
    // ENDPOINTS DE LIBROS
    // ============================================

    /**
     * POST /api/admin/books?adminUserId=1
     * Crear nuevo libro
     */
    @PostMapping("/books")
    public ResponseEntity<ApiResponse<BookDto>> createBook(
            @Valid @RequestBody CreateBookRequest request,
            @RequestParam(value = "adminUserId", required = false) Long adminUserId) {
        
        try {
            verifyAdmin(adminUserId);
            
            BookDto newBook = adminService.createBook(request);
            log.info("Libro creado: {}", newBook.getTitle());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Libro creado exitosamente", newBook));
            
        } catch (RuntimeException e) {
            log.error("Error al crear libro: {}", e.getMessage());
            
            if (e.getMessage().contains("Acceso denegado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PUT /api/admin/books/{bookId}?adminUserId=1
     * Actualizar libro
     */
    @PutMapping("/books/{bookId}")
    public ResponseEntity<ApiResponse<BookDto>> updateBook(
            @PathVariable Long bookId,
            @Valid @RequestBody UpdateBookRequest request,
            @RequestParam(value = "adminUserId", required = false) Long adminUserId) {
        
        try {
            verifyAdmin(adminUserId);
            
            BookDto updatedBook = adminService.updateBook(bookId, request);
            log.info("Libro actualizado: {}", updatedBook.getTitle());
            
            return ResponseEntity.ok(ApiResponse.success("Libro actualizado", updatedBook));
            
        } catch (RuntimeException e) {
            log.error("Error al actualizar libro: {}", e.getMessage());
            
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            if (e.getMessage().contains("Acceso denegado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/admin/books/{bookId}?adminUserId=1
     * Eliminar libro
     */
    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(
            @PathVariable Long bookId,
            @RequestParam(value = "adminUserId", required = false) Long adminUserId) {
        
        try {
            verifyAdmin(adminUserId);
            
            adminService.deleteBook(bookId);
            log.info("Libro eliminado: ID={}", bookId);
            
            return ResponseEntity.ok(ApiResponse.success("Libro eliminado exitosamente", null));
            
        } catch (RuntimeException e) {
            log.error("Error al eliminar libro: {}", e.getMessage());
            
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
