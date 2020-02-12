package com.training.controller;
import com.training.entity.User;
import com.training.service.UserService;
import com.training.service.exceptions.UserExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class WebPagesController {
    private UserService userService;

    @Autowired
    public WebPagesController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        String locale = LocaleContextHolder.getLocale().getLanguage();
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping(value = "/registration")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            userService.registerNewUser(user);
        } catch (UserExistException ex) {
            model.addAttribute("error", true);
            return "registration";
        }
        return "redirect:/login";
    }
}
