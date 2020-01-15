package com.training.controller;

import com.training.dto.*;
import com.training.service.AuthorService;
import com.training.service.BookRequestService;
import com.training.service.BookService;
import com.training.service.RecordService;
import com.training.service.exceptions.AuthorNotFoundException;
import com.training.service.exceptions.BookNotAvailableException;
import com.training.service.exceptions.NoSuchRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AdminController {
    private static final int PAGE_SIZE = 10;

    private static final Logger log = LogManager.getLogger();

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRequestService bookRequestService;

    @Autowired
    private RecordService recordService;

    @GetMapping(value = "/admin/addbook")
    public String addBook(Model model) {
        model.addAttribute("book", new BookDTO());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "/admin/addbook";
    }

    @PostMapping(value = "/admin/addbook")
    public String addBook(@Valid @ModelAttribute("book") BookDTO book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.getAllAuthors());
            return "/admin/addbook";
        }
        bookService.addNewBook(book);
        return "redirect:/admin/allbooks";
    }

    @GetMapping(value = "/admin/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        try {
            putBookAndAuthors(model, id);
        } catch (BookNotAvailableException e) {
            return "redirect:/error/404";
        }
        return "/admin/edit";
    }

    @PostMapping(value = "/admin/edit/{id}")
    public String saveBook(@Valid @ModelAttribute("book") BookDTO book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.getAllAuthors());
            return "/admin/edit";
        }
        try {
            bookService.editBook(book);
        } catch (BookNotAvailableException | AuthorNotFoundException e) {
            return "redirect:/error/404";
        }
        return "redirect:/admin/allbooks";
    }

    @PostMapping(value = "/admin/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        try {
            bookService.deleteBook(id);
        } catch (BookNotAvailableException ex) {
            log.error(ex);
            return "redirect:/admin/allbooks";
        }
        return "redirect:/admin/allbooks";
    }

    @GetMapping(value = "/admin/addauthor")
    public String addAuthor(Model model) {
        model.addAttribute("author", new AuthorDTO());
        return "/admin/addauthor";
    }

    @PostMapping(value = "/admin/addauthor")
    public String addAuthor(@Valid @ModelAttribute("author") AuthorDTO author, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/admin/addauthor";
        }
        authorService.addNewAuthor(author);
        return "redirect:/admin/addbook";
    }

    @GetMapping(value = "/admin/requests")
    public String showRequests(@RequestParam("page") Optional<Integer> page, Model model) {
        int currentPage = page.orElse(1);
        Optional<Page<BookRequestDTO>> bookRequestDTOPage =
                bookRequestService.getAllRequests(PageRequest.of(currentPage - 1, PAGE_SIZE, Sort.by("requestDate").descending()));
        if (bookRequestDTOPage.isPresent()) {
            model.addAttribute("requests", bookRequestDTOPage.get());
            int totalPages = bookRequestDTOPage.get().getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("empty", false);
                Boolean action = false;
                model.addAttribute("action", action);
            }
        } else {
            model.addAttribute("empty", true);
        }
        return "/admin/requests";
    }

    @PostMapping(value = "/admin/approve/{id}")
    public String bookRequestAction(@PathVariable("id") Long id, @ModelAttribute("action") Boolean action) {
        try {
            bookRequestService.processRequest(id, action);
        } catch (NoSuchRequestException | BookNotAvailableException e) {
            return "redirect:/admin/requests";
        }
        return "redirect:/admin/requests";
    }

    @GetMapping(value = "/admin/stats")
    public String showStatistics(@RequestParam("page") Optional<Integer> page, Model model) {
        int currentPage = page.orElse(1);
        Optional<Page<RecordDTO>> records = recordService.getAllRecords(PageRequest.of(currentPage - 1, PAGE_SIZE));
        if(records.isPresent()){
            model.addAttribute("sats", records.get());
            int totalPages = records.get().getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("stats", records.get());
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("empty", false);
            }
        } else {
            model.addAttribute("empty", true);
        }
        return "/admin/stats";
    }


    @Transactional
    void putBookAndAuthors(Model model, Long id) throws BookNotAvailableException {
        model.addAttribute("book", bookService.getBookDTOById(id));
        model.addAttribute("authors", authorService.getAllAuthors());
        log.debug("test");
    }




}
