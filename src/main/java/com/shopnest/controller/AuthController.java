package com.shopnest.controller;

import com.shopnest.model.User;
import com.shopnest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String required, Model model) {
        if (required != null) {
            model.addAttribute("error", "Please log in to continue.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, Model model) {
        User user = userService.login(email, password);
        if (user == null) {
            model.addAttribute("error", "Wrong email or password. Please try again.");
            return "login";
        }
        session.setAttribute("userId", user.getId());
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email,
                           @RequestParam String phone, @RequestParam String password,
                           Model model, RedirectAttributes redirect) {
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "register";
        }
        if (userService.emailExists(email)) {
            model.addAttribute("error", "This email is already registered. Try logging in.");
            return "register";
        }
        userService.register(name, email, phone, password);
        redirect.addFlashAttribute("success", "Account created! Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
