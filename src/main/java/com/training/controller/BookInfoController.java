package com.training.controller;

import com.training.service.BookService;
import com.training.service.exceptions.BookNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookInfoController {
    private BookService bookService;

    @Autowired
    public BookInfoController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/user/book/{id}")
    public String bookInfoUser(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("book", bookService.getBookInfoById(id));
        } catch (BookNotAvailableException e) {
            return "redirect:/user/allbooks";
        }
        return "/user/bookinfo";
    }

    @GetMapping(value = "/admin/book/{id}")
    public String bookInfoAdmin(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("book", bookService.getBookInfoById(id));
        } catch (BookNotAvailableException e) {
            return "redirect:/admin/allbooks";
        }
        return "/admin/bookinfo";
    }
}
