package com.shopnest.config;

import com.shopnest.model.User;
import com.shopnest.service.CartService;
import com.shopnest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Adds the logged-in user and cart count to every page (used by the navbar). */
@ControllerAdvice
public class GlobalModelAdvice {

    private final UserService userService;
    private final CartService cartService;

    public GlobalModelAdvice(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @ModelAttribute
    public void addCommonData(Model model, HttpSession session) {
        User user = userService.currentUser(session);
        int count = 0;
        if (user != null) {
            count = cartService.getItems(user).stream().mapToInt(i -> i.getQuantity()).sum();
        }
        model.addAttribute("loggedUser", user);
        model.addAttribute("cartCount", count);
    }
}
