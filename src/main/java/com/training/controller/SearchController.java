package com.training.controller;

import com.training.entity.Book;
import com.training.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SearchController {

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
    public String userSearchResult(@ModelAttribute("book") Book book, Model model) {
        makeSearch(book, model);
        return "user/searchresult";
    }

    @GetMapping(value = "/admin/search")
    public String adminSearchPage(Model model) {
        model.addAttribute("book", new Book());
        return "admin/search";
    }

    @PostMapping(value = "/admin/search/")
    public String adminSearchResult(@ModelAttribute("book") Book book, Model model) {
        makeSearch(book, model);
        return "admin/searchresult";
    }

    private void makeSearch(Book book, Model model) {
        List<Book> books = bookService.search(book);
        model.addAttribute("books", books);
    }
}
