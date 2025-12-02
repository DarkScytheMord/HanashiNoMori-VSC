package com.HanashiNoMori.HanashiNoMori.Repository;

import com.HanashiNoMori.HanashiNoMori.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategory(String category);
    List<Book> findByTitleContainingIgnoreCase(String title);
}
