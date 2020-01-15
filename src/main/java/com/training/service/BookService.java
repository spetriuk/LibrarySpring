package com.training.service;

import com.training.dto.BookDTO;
import com.training.dto.BookMapper;
import com.training.dto.ShowBookDTO;
import com.training.entity.Book;
import com.training.entity.User;
import com.training.repository.BookRepository;
import com.training.service.exceptions.AuthorNotFoundException;
import com.training.service.exceptions.BookNotAvailableException;
import com.training.service.exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private static final Logger log = LogManager.getLogger();

    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    AuthorService authorService;

    @Autowired
    UserService userService;

    @Autowired RecordService recordService;


    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
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

    @Transactional
    public Optional<Page<ShowBookDTO>> getAllBooks(Pageable pageable){
        Page<Book> books = bookRepository.findAll(pageable);
        if (books.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(books.map(bookMapper::convertToShowBookDTO));
    }

    @Transactional
    public BookDTO getBookDTOById(Long id) throws BookNotAvailableException {
        return bookMapper.convertToDto(bookRepository.findBookById(id).orElseThrow(BookNotAvailableException::new));
    }

    public Book getBookById(Long id) throws BookNotAvailableException {
        return bookRepository.findBookById(id).orElseThrow(BookNotAvailableException::new);
    }

    public ShowBookDTO getBookInfoById(Long id) throws BookNotAvailableException {
        return bookMapper.convertToShowBookDTO(bookRepository.findBookById(id).orElseThrow(BookNotAvailableException::new));
    }

    @Transactional
    public void editBook(BookDTO bookDTO) throws BookNotAvailableException, AuthorNotFoundException {
        Book book = bookRepository.findBookById(bookDTO.getId()).orElseThrow(BookNotAvailableException::new);
        if(book.getAvailable()){
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
    public void setReader(Long bookId, Long userId) throws UserNotFoundException, BookNotAvailableException {
        Book book = bookRepository.findBookById(bookId).orElseThrow(BookNotAvailableException::new);
        User user = userService.getUserById(userId);
        book.setReader(user);
    }

    public Optional<Page<ShowBookDTO>> getAllBooksByUser(Principal principal, Pageable pageable) throws UserNotFoundException {
        Page<Book> bookPage = bookRepository.findAllByReaderId(userService.getUserByEmail(principal.getName()).getId(), pageable);
        if(bookPage.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(bookPage.map(bookMapper::convertToShowBookDTO));
    }

    @Transactional
    public void returnBook(Long id) throws BookNotAvailableException {
        Book book = bookRepository.findBookById(id).orElseThrow(BookNotAvailableException::new);
        book.setAvailable(true);
        book.setExpDate(null);
        book.setReader(null);
        recordService.addReturnDate(id);

    }

    @Transactional
    public Page<Book> search(Book book, Pageable pageable) {
        ExampleMatcher customExampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("nameUkr", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("nameEng", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Book> bookExample = Example.of(book, customExampleMatcher);
        return bookRepository.findAll(bookExample, pageable);
    }
}
