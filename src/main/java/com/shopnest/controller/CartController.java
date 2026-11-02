package com.shopnest.controller;

import com.shopnest.model.CartItem;
import com.shopnest.model.Product;
import com.shopnest.model.User;
import com.shopnest.repository.CartItemRepository;
import com.shopnest.repository.ProductRepository;
import com.shopnest.service.CartService;
import com.shopnest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {

    private final UserService userService;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartController(UserService userService, CartService cartService,
                          CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        User user = userService.currentUser(session);
        List<CartItem> items = cartService.getItems(user);
        double subtotal = cartService.subtotal(items);
        double delivery = cartService.delivery(subtotal);

        model.addAttribute("items", items);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("delivery", delivery);
        model.addAttribute("total", subtotal + delivery);
        model.addAttribute("freeAbove", CartService.FREE_DELIVERY_ABOVE);
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session, HttpServletRequest request,
                            RedirectAttributes redirect) {
        User user = userService.currentUser(session);
        Product product = productRepository.findById(productId).orElse(null);
        String back = request.getHeader("Referer");
        String target = (back != null) ? "redirect:" + back : "redirect:/products";

        if (product == null) {
            return "redirect:/products";
        }
        if (product.getStock() <= 0) {
            redirect.addFlashAttribute("error", product.getName() + " is out of stock.");
            return target;
        }

        CartItem item = cartItemRepository.findByUserAndProduct(user, product).orElse(null);
        if (item == null) {
            item = new CartItem();
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(0);
        }
        int newQty = Math.min(item.getQuantity() + Math.max(quantity, 1), product.getStock());
        item.setQuantity(newQty);
        cartItemRepository.save(item);

        redirect.addFlashAttribute("success", product.getName() + " added to your cart.");
        return target;
    }

    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long itemId, @RequestParam int quantity,
                                 HttpSession session, RedirectAttributes redirect) {
        User user = userService.currentUser(session);
        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null && item.getUser().getId().equals(user.getId())) {
            if (quantity <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(Math.min(quantity, item.getProduct().getStock()));
                cartItemRepository.save(item);
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam Long itemId, HttpSession session, RedirectAttributes redirect) {
        User user = userService.currentUser(session);
        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null && item.getUser().getId().equals(user.getId())) {
            cartItemRepository.delete(item);
            redirect.addFlashAttribute("success", "Item removed from your cart.");
        }
        return "redirect:/cart";
    }
}
