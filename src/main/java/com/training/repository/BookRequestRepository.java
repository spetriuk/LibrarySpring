package com.training.repository;

import com.training.entity.BookRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
    Page<BookRequest> findAllByUserId(Long id, Pageable pageable);
    List<BookRequest> findAllByBookIdAndApprovedIsNull(Long id);
    Optional<BookRequest> findById(Long id);
}
