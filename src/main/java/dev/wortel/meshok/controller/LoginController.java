package dev.wortel.meshok.controller;

import dev.wortel.meshok.dto.LoginForm;
import dev.wortel.meshok.dto.RegistrationForm;
import dev.wortel.meshok.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String getPage(Model model) {
        log.debug("Get login page");
        return "login";
    }

//    @PostMapping
//    public String login(
//            @Valid LoginForm loginForm, BindingResult bindingResult) {
//        log.debug("Post login page");
//        if (bindingResult.hasErrors()) {
//            return "login";
//        }
//
//        if (service.login(loginForm)) {
//            log.debug("Login successful");
//            return "redirect:/items";
//        }
//
//        return "login";
//    }
}