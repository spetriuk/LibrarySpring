package com.training.controller;

import com.training.dto.ShowBookDTO;
import com.training.entity.Book;
import com.training.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AllBooksController {
    private static final int PAGE_SIZE = 10;
    private BookService bookService;

    @Autowired
    public AllBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/admin/allbooks")
    public String adminAllBooks(@RequestParam("page") Optional<Integer> page, Model model) {
        showAllBooks(page, model);
        return "/admin/allbooks";
    }

    @GetMapping(value = "/user/allbooks")
    public String userAllBooks(@RequestParam("page") Optional<Integer> page, Model model) {
        showAllBooks(page, model);
        return "/user/allbooks";
    }

    private void showAllBooks(@RequestParam("page") Optional<Integer> page, Model model) {
        int currentPage = page.orElse(1);
        Optional<Page<ShowBookDTO>> booksPage = bookService.getAllBooks(PageRequest.of(currentPage - 1, PAGE_SIZE));
        if(booksPage.isPresent()) {
            model.addAttribute("books", booksPage.get());
            int totalPages = booksPage.get().getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("empty", false);
            }
        } else {
            model.addAttribute("empty", true);
        }
    }


}
