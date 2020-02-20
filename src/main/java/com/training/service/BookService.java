package com.training.service;

import com.training.dto.BookDTO;
import com.training.dto.ShowBookDTO;
import com.training.entity.Book;
import com.training.repository.BookRepository;
import com.training.service.exceptions.AuthorNotFoundException;
import com.training.service.exceptions.BookNotAvailableException;
import com.training.service.exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.security.Principal;
import java.util.List;

@Service
public class BookService {
    private static final Logger log = LogManager.getLogger();
    private BookRepository bookRepository;
    private AuthorService authorService;
    private UserService userService;
    private RecordService recordService;
    private DtoConversionService dtoConversionService;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService, UserService userService,
                       RecordService recordService, DtoConversionService dtoConversionService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.userService = userService;
        this.recordService = recordService;
        this.dtoConversionService = dtoConversionService;
    }

    public void addNewBook(BookDTO bookDTO) {
        Book book;
        try {
            book = Book.builder()
                    .nameUkr(bookDTO.getNameUkr())
                    .nameEng(bookDTO.getNameEng())
                    .available(true)
                    .expDate(null)
                    .genres(bookDTO.getGenres())
                    .author(authorService.getAuthorById(bookDTO.getAuthorId()))
                    .reader(null)
                    .build();
            bookRepository.save(book);
        } catch (AuthorNotFoundException e) {
            log.error(bookDTO.getAuthorId(), e);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public Page<ShowBookDTO> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(dtoConversionService::convertToShowBookDTO);
    }

    public BookDTO getBookDTOById(Long id) throws BookNotAvailableException {
        return dtoConversionService.convertToBookDto(bookRepository.findBookById(id).orElseThrow(BookNotAvailableException::new));
    }

    @Transactional
    public Book getBookById(Long id) throws BookNotAvailableException {
        return bookRepository.findBookById(id).orElseThrow(BookNotAvailableException::new);
    }

    public ShowBookDTO getBookInfoById(Long id) throws BookNotAvailableException {
        return dtoConversionService.convertToShowBookDTO(bookRepository.findBookById(id).orElseThrow(BookNotAvailableException::new));
    }

    @Transactional
    public void editBook(BookDTO bookDTO) throws BookNotAvailableException, AuthorNotFoundException, OptimisticLockException {
        Book book = bookRepository.findBookById(bookDTO.getId()).orElseThrow(BookNotAvailableException::new);
        if (book.getAvailable()) {
            book.setNameUkr(bookDTO.getNameUkr());
            book.setNameEng(bookDTO.getNameEng());
            book.setGenres(bookDTO.getGenres());
            book.setAuthor(authorService.getAuthorById(bookDTO.getAuthorId()));
        }
    }

    public void deleteBook(Long id) throws BookNotAvailableException {
        try {
            bookRepository.deleteById(id);
        } catch (Exception ex) {
            throw new BookNotAvailableException();
        }
    }

    @Transactional
    public Page<ShowBookDTO> getAllBooksByUser(Principal principal, Pageable pageable) throws UserNotFoundException {
        Page<Book> bookPage = bookRepository.findAllByReaderId(userService.getUserByEmail(principal.getName()).getId(), pageable);
        return bookPage.map(dtoConversionService::convertToShowBookDTO);
    }

    @Transactional
    public void returnBook(Long id) throws BookNotAvailableException {
        Book book = bookRepository.findBookById(id).orElseThrow(BookNotAvailableException::new);
        book.setAvailable(true);
        book.setExpDate(null);
        book.setReader(null);
        recordService.addReturnDate(id);
    }

    public List<Book> search(Book book) {
        ExampleMatcher customExampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("nameUkr", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("nameEng", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Book> bookExample = Example.of(book, customExampleMatcher);
        return bookRepository.findAll(bookExample);
    }
}
