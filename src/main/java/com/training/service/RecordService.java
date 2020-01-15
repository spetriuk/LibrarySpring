package com.training.service;

import com.training.dto.BookMapper;
import com.training.dto.RecordDTO;
import com.training.entity.Book;
import com.training.entity.Record;
import com.training.entity.User;
import com.training.repository.RecordRepository;
import com.training.service.exceptions.BookNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RecordService {

    private RecordRepository recordRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public void addRecord(Book book, User user) throws BookNotAvailableException {
        Record record = Record.builder()
                .book(book)
                .user(user)
                .takeDate(LocalDateTime.now())
                .returnDate(null)
                .build();
        recordRepository.save(record);
    }

    public Optional<Page<RecordDTO>> getAllRecords(Pageable pageable) {
        Page<Record> recordPage = recordRepository.findAll(pageable);
        if (recordPage.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(recordPage.map(bookMapper::convertToRecordDTO));
    }

    @Transactional
    public void addReturnDate(Long bookId) {
        Optional<Record> record = recordRepository.findByBookIdAndReturnDateIsNull(bookId);
        if (record.isPresent()) {
            Record recordToEdit = record.get();
            recordToEdit.setReturnDate(LocalDateTime.now());
        }
    }
}
