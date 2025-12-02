package com.HanashiNoMori.HanashiNoMori.Controller;

import com.HanashiNoMori.HanashiNoMori.DTO.AddFavoriteRequest;
import com.HanashiNoMori.HanashiNoMori.DTO.ApiResponse;
import com.HanashiNoMori.HanashiNoMori.DTO.FavoriteDto;
import com.HanashiNoMori.HanashiNoMori.DTO.ToggleReadRequest;
import com.HanashiNoMori.HanashiNoMori.Service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<FavoriteDto>>> getUserFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(userId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteDto>> addFavorite(@RequestBody AddFavoriteRequest request) {
        return ResponseEntity.ok(favoriteService.addFavorite(request));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(@PathVariable Long favoriteId) {
        return ResponseEntity.ok(favoriteService.removeFavorite(favoriteId));
    }

    @PutMapping("/{favoriteId}/toggle-read")
    public ResponseEntity<ApiResponse<FavoriteDto>> toggleReadStatus(
        @PathVariable Long favoriteId,
        @RequestBody ToggleReadRequest request
    ) {
        return ResponseEntity.ok(favoriteService.toggleReadStatus(favoriteId, request));
    }

    @GetMapping("/check/{userId}/{bookId}")
    public ResponseEntity<ApiResponse<FavoriteDto>> checkFavorite(
        @PathVariable Long userId,
        @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(favoriteService.checkFavorite(userId, bookId));
    }
}
