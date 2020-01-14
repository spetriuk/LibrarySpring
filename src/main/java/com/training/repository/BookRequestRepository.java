package com.training.repository;

import com.training.entity.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
    List<BookRequest> findAllByUserId(Long id);
    List<BookRequest> findAllByBookIdAndApprovedIsNull(Long id);
    Optional<BookRequest> findById(Long id);
}
