package com.training.repository;

import com.training.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<Record> findByBookIdAndReturnDateIsNull(Long bookId);
}

