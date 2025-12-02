package com.HanashiNoMori.HanashiNoMori.Controller;

import com.HanashiNoMori.HanashiNoMori.DTO.ApiResponse;
import com.HanashiNoMori.HanashiNoMori.DTO.BookDto;
import com.HanashiNoMori.HanashiNoMori.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookDto>>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookDto>>> searchBooks(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<BookDto>>> getBooksByCategory(@PathVariable String category) {
        return ResponseEntity.ok(bookService.getBooksByCategory(category));
    }
}
