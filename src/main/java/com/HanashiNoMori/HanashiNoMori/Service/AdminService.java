package com.HanashiNoMori.HanashiNoMori.Service;

import com.HanashiNoMori.HanashiNoMori.DTO.*;
import com.HanashiNoMori.HanashiNoMori.Model.Book;
import com.HanashiNoMori.HanashiNoMori.Model.User;
import com.HanashiNoMori.HanashiNoMori.Repository.BookRepository;
import com.HanashiNoMori.HanashiNoMori.Repository.FavoriteRepository;
import com.HanashiNoMori.HanashiNoMori.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final FavoriteRepository favoriteRepository;
    private final PasswordEncoder passwordEncoder;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // ============================================
    // GESTION DE USUARIOS
    // ============================================

    /**
     * Obtener todos los usuarios
     */
    public List<UserDto> getAllUsers() {
        log.info("Obteniendo todos los usuarios");
        return userRepository.findAll().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un usuario por ID
     */
    public UserDto getUserById(Long userId) {
        log.info("Obteniendo usuario con ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToUserDto(user);
    }

    /**
     * Crear nuevo usuario
     */
    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        log.info("Creando nuevo usuario: {}", request.getUsername());

        // Validar username unico
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El username ya esta en uso");
        }

        // Validar email unico
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya esta en uso");
        }

        // Crear usuario
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // BCrypt
                .isAdmin(request.getIsAdmin() != null ? request.getIsAdmin() : false)
                .fullName(request.getUsername())
                .build();

        user = userRepository.save(user);
        log.info("Usuario creado exitosamente: ID={}", user.getId());

        return convertToUserDto(user);
    }

    /**
     * Actualizar usuario
     */
    @Transactional
    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        log.info("Actualizando usuario ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar username si se proporciona
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            // Validar que el nuevo username no este en uso por otro usuario
            userRepository.findByUsername(request.getUsername()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userId)) {
                    throw new RuntimeException("El username ya esta en uso");
                }
            });
            user.setUsername(request.getUsername());
        }

        // Actualizar email si se proporciona
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            // Validar que el nuevo email no este en uso por otro usuario
            userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userId)) {
                    throw new RuntimeException("El email ya esta en uso");
                }
            });
            user.setEmail(request.getEmail());
        }

        // Actualizar password si se proporciona
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt
        }

        // Actualizar isAdmin si se proporciona
        if (request.getIsAdmin() != null) {
            user.setIsAdmin(request.getIsAdmin());
        }

        user = userRepository.save(user);
        log.info("Usuario actualizado exitosamente: ID={}", user.getId());

        return convertToUserDto(user);
    }

    /**
     * Eliminar usuario
     */
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Eliminando usuario ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que no sea el ultimo admin
        if (user.getIsAdmin() != null && user.getIsAdmin()) {
            long adminCount = userRepository.countByIsAdmin(true);
            if (adminCount <= 1) {
                throw new RuntimeException("No se puede eliminar el ultimo administrador");
            }
        }

        // Eliminar favoritos del usuario (cascade deberia hacerlo automaticamente)
        favoriteRepository.deleteByUserId(userId);

        // Eliminar usuario
        userRepository.delete(user);
        log.info("Usuario eliminado exitosamente: ID={}", userId);
    }

    // ============================================
    // GESTION DE LIBROS
    // ============================================

    /**
     * Crear nuevo libro
     */
    @Transactional
    public BookDto createBook(CreateBookRequest request) {
        log.info("Creando nuevo libro: {}", request.getTitle());

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .category(request.getCategory())
                .description(request.getDescription())
                .coverUrl(request.getCoverUrl())
                .isbn(request.getIsbn())
                .build();

        book = bookRepository.save(book);
        log.info("Libro creado exitosamente: ID={}", book.getId());

        return convertToBookDto(book);
    }

    /**
     * Actualizar libro
     */
    @Transactional
    public BookDto updateBook(Long bookId, UpdateBookRequest request) {
        log.info("Actualizando libro ID: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        // Actualizar solo los campos que no sean null
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null && !request.getAuthor().isEmpty()) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            book.setCategory(request.getCategory());
        }
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }
        if (request.getCoverUrl() != null) {
            book.setCoverUrl(request.getCoverUrl());
        }
        if (request.getIsbn() != null) {
            book.setIsbn(request.getIsbn());
        }

        book = bookRepository.save(book);
        log.info("Libro actualizado exitosamente: ID={}", book.getId());

        return convertToBookDto(book);
    }

    /**
     * Eliminar libro
     */
    @Transactional
    public void deleteBook(Long bookId) {
        log.info("Eliminando libro ID: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        // Eliminar favoritos relacionados
        favoriteRepository.deleteByBookId(bookId);

        // Eliminar libro
        bookRepository.delete(book);
        log.info("Libro eliminado exitosamente: ID={}", bookId);
    }

    // ============================================
    // METODOS AUXILIARES
    // ============================================

    private UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isAdmin(user.getIsAdmin() != null ? user.getIsAdmin() : false)
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null)
                .build();
    }

    private BookDto convertToBookDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .category(book.getCategory())
                .description(book.getDescription())
                .coverUrl(book.getCoverUrl())
                .isbn(book.getIsbn())
                .build();
    }
}
