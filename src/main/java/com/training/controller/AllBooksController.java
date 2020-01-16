package com.training.controller;

import com.training.dto.ShowBookDTO;
import com.training.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AllBooksController {
    private BookService bookService;

    @Autowired
    public AllBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/admin/allbooks")
    public String adminAllBooks(@PageableDefault Pageable pageable, Model model) {
        showAllBooks(pageable, model);
        return "/admin/allbooks";
    }

    @GetMapping(value = "/user/allbooks")
    public String userAllBooks(@PageableDefault Pageable pageable, Model model) {
        showAllBooks(pageable, model);
        return "/user/allbooks";
    }

    private void showAllBooks(Pageable pageable, Model model) {
        Page<ShowBookDTO> booksPage = bookService.getAllBooks(pageable);
        model.addAttribute("books", booksPage);
    }


}
