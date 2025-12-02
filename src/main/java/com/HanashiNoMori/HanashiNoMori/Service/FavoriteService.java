package com.HanashiNoMori.HanashiNoMori.Service;

import com.HanashiNoMori.HanashiNoMori.DTO.AddFavoriteRequest;
import com.HanashiNoMori.HanashiNoMori.DTO.ApiResponse;
import com.HanashiNoMori.HanashiNoMori.DTO.BookDto;
import com.HanashiNoMori.HanashiNoMori.DTO.FavoriteDto;
import com.HanashiNoMori.HanashiNoMori.DTO.ToggleReadRequest;
import com.HanashiNoMori.HanashiNoMori.Model.Book;
import com.HanashiNoMori.HanashiNoMori.Model.Favorite;
import com.HanashiNoMori.HanashiNoMori.Model.User;
import com.HanashiNoMori.HanashiNoMori.Repository.BookRepository;
import com.HanashiNoMori.HanashiNoMori.Repository.FavoriteRepository;
import com.HanashiNoMori.HanashiNoMori.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public ApiResponse<List<FavoriteDto>> getUserFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        List<FavoriteDto> favoriteDtos = favorites.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ApiResponse.success("Favoritos obtenidos", favoriteDtos);
    }

    public ApiResponse<FavoriteDto> addFavorite(AddFavoriteRequest request) {
        if (favoriteRepository.existsByUserIdAndBookId(request.getUserId(), request.getBookId())) {
            return ApiResponse.error("El libro ya está en favoritos");
        }

        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Book book = bookRepository.findById(request.getBookId())
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        Favorite favorite = Favorite.builder()
            .user(user)
            .book(book)
            .isRead(false)
            .build();

        favorite = favoriteRepository.save(favorite);

        return ApiResponse.success("Agregado a favoritos", convertToDto(favorite));
    }

    @Transactional
    public ApiResponse<Void> removeFavorite(Long favoriteId) {
        if (!favoriteRepository.existsById(favoriteId)) {
            return ApiResponse.error("Favorito no encontrado");
        }
        favoriteRepository.deleteById(favoriteId);
        return ApiResponse.success("Favorito eliminado", null);
    }

    public ApiResponse<FavoriteDto> toggleReadStatus(Long favoriteId, ToggleReadRequest request) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
            .orElseThrow(() -> new RuntimeException("Favorito no encontrado"));

        favorite.setIsRead(request.getIsRead());
        favorite = favoriteRepository.save(favorite);

        return ApiResponse.success("Estado actualizado", convertToDto(favorite));
    }

    public ApiResponse<FavoriteDto> checkFavorite(Long userId, Long bookId) {
        Favorite favorite = favoriteRepository.findByUserIdAndBookId(userId, bookId)
            .orElse(null);
        
        if (favorite == null) {
            return ApiResponse.error("Libro no está en favoritos");
        }
        
        return ApiResponse.success("Libro encontrado en favoritos", convertToDto(favorite));
    }

    private FavoriteDto convertToDto(Favorite favorite) {
        BookDto bookDto = BookDto.builder()
            .id(favorite.getBook().getId())
            .title(favorite.getBook().getTitle())
            .author(favorite.getBook().getAuthor())
            .category(favorite.getBook().getCategory())
            .description(favorite.getBook().getDescription())
            .coverUrl(favorite.getBook().getCoverUrl())
            .build();

        return FavoriteDto.builder()
            .id(favorite.getId())
            .userId(favorite.getUser().getId())
            .bookId(favorite.getBook().getId())
            .book(bookDto)
            .isRead(favorite.getIsRead())
            .addedAt(favorite.getAddedAt().toString())
            .build();
    }
}
