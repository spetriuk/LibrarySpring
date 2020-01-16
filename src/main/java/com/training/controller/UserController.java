package com.training.controller;

import com.training.dto.BookRequestDTO;
import com.training.dto.ShowBookDTO;
import com.training.service.BookRequestService;
import com.training.service.BookService;
import com.training.service.exceptions.BookNotAvailableException;
import com.training.service.exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class UserController {
    private static final Logger log = LogManager.getLogger();

    private BookRequestService bookRequestService;
    private BookService bookService;

    @Autowired
    public UserController(BookRequestService bookRequestService, BookService bookService) {
        this.bookRequestService = bookRequestService;
        this.bookService = bookService;
    }

    @GetMapping(value = "/user/add/{id}")
    public String saveBook(@PathVariable("id") Long id, Model model, Principal principal) {
        try {
            bookRequestService.addRequest(id, principal);
        } catch (UserNotFoundException ex) {
            log.error(principal.getName(), ex);
            return "redirect:/user/requests";
        } catch (BookNotAvailableException ex) {
            log.error(id, ex);
            return "redirect:/user/requests";
        }
        return "redirect:/user/requests";
    }

    @GetMapping(value = "/user/requests")
    public String showRequests(@PageableDefault(sort = "requestDate", direction = Sort.Direction.DESC) Pageable pageable, Model model, Principal principal) {
        try {
            Page<BookRequestDTO> requests = bookRequestService.getAllUserRequests(principal, pageable);
            model.addAttribute("requests", requests);
        } catch (UserNotFoundException ex) {
            log.error(principal.getName(), ex);
            return "redirect:/user/requests";
        }
        return "/user/requests";
    }

    @GetMapping(value = "user/mybooks")
    public String showMyBooks(@PageableDefault(sort = "expDate", direction = Sort.Direction.DESC) Pageable pageable, Model model, Principal principal) {
        try {
            Page<ShowBookDTO> books = bookService.getAllBooksByUser(principal, pageable);
            model.addAttribute("books", books);
        } catch (UserNotFoundException ex) {
            log.error(principal.getName(), ex);
            return "redirect:/user/mybooks";
        }
        return "user/mybooks";
    }

    @GetMapping(value = "/user/return/{id}")
    public String returnBook(@PathVariable("id") Long id) {
        try {
            bookService.returnBook(id);
        } catch (BookNotAvailableException ex) {
            log.error(id, ex);
        }
        return "redirect:/user/mybooks";
    }

}
