package com.shopnest.controller;

import com.shopnest.model.User;
import com.shopnest.repository.UserRepository;
import com.shopnest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;
    private final UserRepository userRepository;

    public ProfileController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        model.addAttribute("user", userService.currentUser(session));
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name, @RequestParam String phone,
                                @RequestParam String address, HttpSession session,
                                RedirectAttributes redirect) {
        User user = userService.currentUser(session);
        user.setName(name.trim());
        user.setPhone(phone);
        user.setAddress(address);
        userRepository.save(user);
        redirect.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
                                 HttpSession session, RedirectAttributes redirect) {
        User user = userService.currentUser(session);
        if (!userService.passwordMatches(user, currentPassword)) {
            redirect.addFlashAttribute("error", "Current password is incorrect.");
        } else if (newPassword.length() < 6) {
            redirect.addFlashAttribute("error", "New password must be at least 6 characters.");
        } else {
            userService.changePassword(user, newPassword);
            redirect.addFlashAttribute("success", "Password changed successfully.");
        }
        return "redirect:/profile";
    }
}
