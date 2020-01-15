package com.training.dto;
import static com.training.controller.Constants.DATE_TIME_FORMAT;
import com.training.entity.Author;
import com.training.entity.Book;
import com.training.entity.BookRequest;
import com.training.entity.Record;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authorId", source = "book.author.id")
    BookDTO convertToDto(Book book);

    List<BookDTO> convertToBookDtoList(List<Book> books);

    AuthorDTO convertToDto(Author author);

    @Mapping(target = "books", ignore = true)
    Author convertToEntity(AuthorDTO authorDTO);

    List<AuthorDTO> convertToAuthorDtoList(List<Author> authors);

    @Mappings({
            @Mapping(target = "authorNameUkr", source = "book.author.nameUkr"),
            @Mapping(target = "authorNameEng", source = "book.author.nameEng"),
            @Mapping(target = "expDate", source = "book.expDate", qualifiedByName = "expDate")
    })
    ShowBookDTO convertToShowBookDTO(Book book);

    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "nameUkr", source = "book.nameUkr")
    @Mapping(target = "nameEng", source = "book.nameEng")
    @Mapping(target = "authorNameUkr", source = "book.author.nameUkr")
    @Mapping(target = "authorNameEng", source = "book.author.nameEng")
    @Mapping(target = "requestDate",  qualifiedByName = "expDate")
    BookRequestDTO convertToBookRequestDTO(BookRequest bookRequest);

    @Mapping(target = "reader", source = "user.email")
    @Mapping(target = "nameUkr", source = "book.nameUkr")
    @Mapping(target = "nameEng", source = "book.nameEng")
    @Mapping(target = "authorNameUkr", source = "book.author.nameUkr")
    @Mapping(target = "authorNameEng", source = "book.author.nameEng")
    @Mapping(target = "takeDate",  qualifiedByName = "expDate")
    @Mapping(target = "returnDate",  qualifiedByName = "expDate")
    RecordDTO convertToRecordDTO(Record record);

    @Named("expDate")
    default String dateTimeToString(LocalDateTime expDate) {
        if (expDate == null) {
            return null;
        }
        return expDate.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, LocaleContextHolder.getLocale()));
    }
}


