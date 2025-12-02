package com.HanashiNoMori.HanashiNoMori.Service;

import com.HanashiNoMori.HanashiNoMori.DTO.ApiResponse;
import com.HanashiNoMori.HanashiNoMori.DTO.BookDto;
import com.HanashiNoMori.HanashiNoMori.Model.Book;
import com.HanashiNoMori.HanashiNoMori.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public ApiResponse<List<BookDto>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDto> bookDtos = books.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ApiResponse.success("Libros obtenidos", bookDtos);
    }

    public ApiResponse<List<BookDto>> getBooksByCategory(String category) {
        List<Book> books = bookRepository.findByCategory(category);
        List<BookDto> bookDtos = books.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ApiResponse.success("Libros obtenidos", bookDtos);
    }

    public ApiResponse<List<BookDto>> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        List<BookDto> bookDtos = books.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ApiResponse.success("Libros encontrados", bookDtos);
    }

    public ApiResponse<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
            .map(book -> ApiResponse.success("Libro encontrado", convertToDto(book)))
            .orElse(ApiResponse.error("Libro no encontrado"));
    }

    private BookDto convertToDto(Book book) {
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
