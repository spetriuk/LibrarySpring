package com.training.controller;

import com.training.entity.Book;
import com.training.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class SearchController {

    private static final int PAGE_SIZE = 10;
    private BookService bookService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Autowired
    public SearchController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/user/search")
    public String userSearchPage(Model model) {
        model.addAttribute("book", new Book());
        return "user/search";
    }

    @PostMapping(value = "/user/search/")
    public String userSearchResult(@RequestParam("page") Optional<Integer> page, @ModelAttribute("book") Book book, Model model) {
        makeSearch(page, book, model);
        return "user/searchresult";
    }

    @GetMapping(value = "/admin/search")
    public String adminSearchPage(Model model) {
        model.addAttribute("book", new Book());
        return "admin/search";
    }

    @PostMapping(value = "/admin/search/")
    public String adminSearchResult(@RequestParam("page") Optional<Integer> page, @ModelAttribute("book") Book book, Model model) {
        makeSearch(page, book, model);
        return "admin/searchresult";
    }

    private void makeSearch(@RequestParam("page") Optional<Integer> page, @ModelAttribute("book") Book book, Model model) {
        int currentPage = page.orElse(1);
        Page<Book> books = bookService.search(book, PageRequest.of(currentPage - 1, PAGE_SIZE));
        model.addAttribute("books", books);
        if (books.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, books.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
