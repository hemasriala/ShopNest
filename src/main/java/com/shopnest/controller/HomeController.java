package com.shopnest.controller;

import com.shopnest.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final ProductRepository productRepository;

    public HomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findTop8ByOrderByIdDesc());
        model.addAttribute("categories", productRepository.findAllCategories());
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String sendMessage(RedirectAttributes redirect) {
        // In a real project the message would be saved or emailed.
        redirect.addFlashAttribute("success", "Thanks! Your message has been received. We will reply within 24 hours.");
        return "redirect:/contact";
    }
}
