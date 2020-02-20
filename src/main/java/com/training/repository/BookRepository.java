package com.training.repository;

import com.training.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookById(Long id);

    void deleteById(Long id);

    Page<Book> findAllByReaderId(Long id, Pageable pageable);
}
