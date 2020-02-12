package com.training.service;

import com.training.dto.BookRequestDTO;
import com.training.entity.BookRequest;
import com.training.repository.BookRequestRepository;
import com.training.service.exceptions.BookNotAvailableException;
import com.training.service.exceptions.NoSuchRequestException;
import com.training.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class BookRequestService {
    private BookRequestRepository bookRequestRepository;
    private DtoConversionService dtoConversionService;
    private UserService userService;
    private BookService bookService;
    private RecordService recordService;

    @Autowired
    public BookRequestService(BookRequestRepository bookRequestRepository, DtoConversionService dtoConversionService,
                              UserService userService, BookService bookService, RecordService recordService) {
        this.bookRequestRepository = bookRequestRepository;
        this.dtoConversionService = dtoConversionService;
        this.userService = userService;
        this.bookService = bookService;
        this.recordService = recordService;
    }

    @Transactional
    public void addRequest(Long bookId, Principal principal) throws UserNotFoundException, BookNotAvailableException {
        BookRequest bookRequest = BookRequest.builder()
                .user(userService.getUserByEmail(principal.getName()))
                .book(bookService.getBookById(bookId))
                .requestDate(LocalDateTime.now())
                .approved(null)
                .build();
        bookRequestRepository.save(bookRequest);
    }

    @Transactional
    public Page<BookRequestDTO> getAllUserRequests(Principal principal, Pageable pageable) throws UserNotFoundException {
             return bookRequestRepository.findAllByUserId(userService.getUserByEmail(principal.getName()).getId(), pageable)
                     .map(dtoConversionService::convertToBookRequestDTO);
    }

    @Transactional
    public void processRequest(Long id, Boolean action) throws NoSuchRequestException, BookNotAvailableException {
        BookRequest bookRequest = bookRequestRepository.findById(id).orElseThrow(NoSuchRequestException::new);
        bookRequest.setApproved(action);
        if(action){
            bookRequestRepository.findAllByBookIdAndApprovedIsNull(bookRequest.getBook().getId())
                    .forEach(req->req.setApproved(false));
            bookRequest.getBook().setReader(bookRequest.getUser());
            bookRequest.getBook().setExpDate(LocalDateTime.now().plusMonths(1L));
            bookRequest.getBook().setAvailable(false);
            recordService.addRecord(bookRequest.getBook(), bookRequest.getUser());
        }
    }

    public Page<BookRequestDTO> getAllRequests(Pageable pageable) {
        return bookRequestRepository.findAll(pageable).map(dtoConversionService::convertToBookRequestDTO);
    }
}
