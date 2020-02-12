package com.training.service;

import com.training.dto.AuthorDTO;
import com.training.entity.Author;
import com.training.repository.AuthorRepository;
import com.training.service.exceptions.AuthorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;
    private DtoConversionService dtoConversionService;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, DtoConversionService dtoConversionService) {
        this.authorRepository = authorRepository;
        this.dtoConversionService = dtoConversionService;
    }


    public List<AuthorDTO> getAllAuthors() {
        return (authorRepository.findAll()
                .stream()
                .map(author -> dtoConversionService.convertToAuthorDto(author))
                .collect(Collectors.toList()));
    }

    @Transactional
    public Author getAuthorById(Long id) throws AuthorNotFoundException {
        return authorRepository.findAuthorById(id).orElseThrow(AuthorNotFoundException::new);
    }

    public void addNewAuthor(Author author) {
        authorRepository.save(author);
    }
}
