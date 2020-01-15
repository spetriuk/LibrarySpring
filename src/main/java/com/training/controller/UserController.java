package com.training.controller;

import com.training.dto.BookRequestDTO;
import com.training.dto.ShowBookDTO;
import com.training.entity.Book;
import com.training.service.BookRequestService;
import com.training.service.BookService;
import com.training.service.exceptions.BookNotAvailableException;
import com.training.service.exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {
    private static final int PAGE_SIZE = 10;
    private static final Logger log = LogManager.getLogger();

    @Autowired
    private BookRequestService bookRequestService;

    @Autowired
    private BookService bookService;

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
    public String showRequests(@RequestParam("page") Optional<Integer> page, Model model, Principal principal) {
        int currentPage = page.orElse(1);
        try {
            Optional<Page<BookRequestDTO>> bookRequestDTOPage = bookRequestService.getAllUserRequests(principal, PageRequest.of(currentPage - 1, PAGE_SIZE));
            if (bookRequestDTOPage.isPresent()) {
                model.addAttribute("requests", bookRequestDTOPage.get());
                int totalPages = bookRequestDTOPage.get().getTotalPages();
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
        } catch (UserNotFoundException ex) {
            log.error(principal.getName(), ex);
            return "redirect:/user/requests";
        }
        return "/user/requests";
    }

    @GetMapping(value = "user/mybooks")
    public String showMyBooks(@RequestParam("page") Optional<Integer> page, Model model, Principal principal) {
        try {
            int currentPage = page.orElse(1);
            Optional<Page<ShowBookDTO>> bookDTOS = bookService.getAllBooksByUser(principal, PageRequest.of(currentPage - 1, PAGE_SIZE));
            if(bookDTOS.isPresent()){
                model.addAttribute("books", bookDTOS.get());
                int totalPages = bookDTOS.get().getTotalPages();
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
