package com.training.service;

import com.training.dto.AuthorDTO;
import com.training.dto.BookMapper;
import com.training.entity.Author;
import com.training.repository.AuthorRepository;
import com.training.service.exceptions.AuthorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;


    @Autowired
    private BookMapper bookMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public List<AuthorDTO> getAllAuthors() {
        return bookMapper.convertToAuthorDtoList(authorRepository.findAll());
    }

    @Transactional
    public Author getAuthorById(Long id) throws AuthorNotFoundException {
        return authorRepository.findAuthorById(id).orElseThrow(AuthorNotFoundException::new);
    }

    public void addNewAuthor(AuthorDTO authorDTO) {
        authorRepository.save(bookMapper.convertToEntity(authorDTO));
    }
}
