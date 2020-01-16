package com.training.controller;

import com.training.dto.AuthorDTO;
import com.training.dto.BookDTO;
import com.training.dto.BookRequestDTO;
import com.training.dto.RecordDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;

@Controller
public class AdminController {
    private static final Logger log = LogManager.getLogger();

    private AuthorService authorService;
    private BookService bookService;
    private BookRequestService bookRequestService;
    private RecordService recordService;

    @Autowired
    public AdminController(AuthorService authorService, BookService bookService,
                           BookRequestService bookRequestService, RecordService recordService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.bookRequestService = bookRequestService;
        this.recordService = recordService;
    }

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
            model.addAttribute("book", bookService.getBookDTOById(id));
            model.addAttribute("authors", authorService.getAllAuthors());
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
        } catch (BookNotAvailableException | AuthorNotFoundException | OptimisticLockException e) {
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
    public String showRequests(@PageableDefault(sort = "requestDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<BookRequestDTO> requests = bookRequestService.getAllRequests(pageable);
        model.addAttribute("requests", requests);
        model.addAttribute("action");
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
    public String showStatistics(@PageableDefault(sort = "takeDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<RecordDTO> records = recordService.getAllRecords(pageable);
        model.addAttribute("stats", records);
        return "/admin/stats";
    }


  /*
    @Transactional
    void putBookAndAuthors(Model model, Long id) throws BookNotAvailableException {
        model.addAttribute("book", bookService.getBookDTOById(id));
        model.addAttribute("authors", authorService.getAllAuthors());
    }*/


}
