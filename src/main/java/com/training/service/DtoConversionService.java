package com.training.service;

import com.training.dto.*;
import com.training.entity.Author;
import com.training.entity.Book;
import com.training.entity.BookRequest;
import com.training.entity.Record;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DtoConversionService {
    public static final String LANG_UKR = "uk";
    private String DATE_TIME_FORMAT = "dd MMMM yyyy HH:mm";

    public ShowBookDTO convertToShowBookDTO(Book book) {
        return ShowBookDTO.builder()
                .name(getLocalizedBookName(book))
                .authorName(getLocalizedAuthorName(book.getAuthor()))
                .available(book.getAvailable())
                .expDate(getLocalizedDate(book.getExpDate()))
                .genres(book.getGenres())
                .id(book.getId())
                .build();
    }

    public BookDTO convertToBookDto(Book book) {
        return BookDTO.builder()
                .authorId(book.getAuthor().getId())
                .available(book.getAvailable())
                .genres(book.getGenres())
                .nameEng(book.getNameEng())
                .nameUkr(book.getNameUkr())
                .id(book.getId())
                .build();
    }

    public AuthorDTO convertToAuthorDto(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .name(getLocalizedAuthorName(author))
                .build();
    }

    public BookRequestDTO convertToBookRequestDTO(BookRequest bookRequest) {
        return BookRequestDTO.builder()
                .userEmail(bookRequest.getUser().getEmail())
                .requestDate(getLocalizedDate(bookRequest.getRequestDate()))
                .authorName(getLocalizedAuthorName(bookRequest.getBook().getAuthor()))
                .name(getLocalizedBookName(bookRequest.getBook()))
                .approved(bookRequest.getApproved())
                .id(bookRequest.getId())
                .build();
    }

    public RecordDTO convertToRecordDTO(Record record) {
        return RecordDTO.builder()
                .reader(record.getUser().getEmail())
                .name(getLocalizedBookName(record.getBook()))
                .authorName(getLocalizedAuthorName(record.getBook().getAuthor()))
                .returnDate(getLocalizedDate(record.getReturnDate()))
                .takeDate(getLocalizedDate(record.getTakeDate()))
                .build();
    }

    private String getLocalizedBookName(Book book) {
        return LocaleContextHolder.getLocale().getLanguage().equals(LANG_UKR) ? book.getNameUkr() : book.getNameEng();
    }

    private String getLocalizedAuthorName(Author author) {
        return LocaleContextHolder.getLocale().getLanguage().equals(LANG_UKR) ? author.getNameUkr() : author.getNameEng();
    }

    private String getLocalizedDate(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, LocaleContextHolder.getLocale()));
    }


}
