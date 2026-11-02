package com.shopnest.controller;

import com.shopnest.model.CartItem;
import com.shopnest.model.Order;
import com.shopnest.model.OrderItem;
import com.shopnest.model.Product;
import com.shopnest.model.User;
import com.shopnest.repository.CartItemRepository;
import com.shopnest.repository.OrderRepository;
import com.shopnest.repository.ProductRepository;
import com.shopnest.service.CartService;
import com.shopnest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OrderController {

    private final UserService userService;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(UserService userService, CartService cartService,
                           CartItemRepository cartItemRepository, OrderRepository orderRepository,
                           ProductRepository productRepository) {
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model, RedirectAttributes redirect) {
        User user = userService.currentUser(session);
        List<CartItem> items = cartService.getItems(user);
        if (items.isEmpty()) {
            redirect.addFlashAttribute("error", "Your cart is empty. Add some products first.");
            return "redirect:/cart";
        }
        double subtotal = cartService.subtotal(items);
        double delivery = cartService.delivery(subtotal);

        model.addAttribute("items", items);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("delivery", delivery);
        model.addAttribute("total", subtotal + delivery);
        model.addAttribute("user", user);
        return "checkout";
    }

    @PostMapping("/orders/place")
    @Transactional
    public String placeOrder(@RequestParam String address, @RequestParam String paymentMethod,
                             HttpSession session, RedirectAttributes redirect) {
        User user = userService.currentUser(session);
        List<CartItem> items = cartService.getItems(user);
        if (items.isEmpty()) {
            return "redirect:/cart";
        }

        // Check stock first
        for (CartItem item : items) {
            if (item.getQuantity() > item.getProduct().getStock()) {
                redirect.addFlashAttribute("error", "Only " + item.getProduct().getStock()
                        + " left for " + item.getProduct().getName() + ". Please update your cart.");
                return "redirect:/cart";
            }
        }

        double subtotal = cartService.subtotal(items);
        double delivery = cartService.delivery(subtotal);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(address);
        order.setDeliveryCharge(delivery);
        order.setTotalAmount(subtotal + delivery);

        for (CartItem item : items) {
            Product product = item.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setImageUrl(product.getImageUrl());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            order.getItems().add(orderItem);

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);
        cartItemRepository.deleteAll(items);

        redirect.addFlashAttribute("success", "Order placed successfully! Thank you for shopping with ShopNest.");
        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/orders")
    public String myOrders(HttpSession session, Model model) {
        User user = userService.currentUser(session);
        model.addAttribute("orders", orderRepository.findByUserOrderByOrderDateDesc(user));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        User user = userService.currentUser(session);
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "order-detail";
    }

    @PostMapping("/orders/{id}/cancel")
    @Transactional
    public String cancelOrder(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        User user = userService.currentUser(session);
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null && order.getUser().getId().equals(user.getId())
                && "PLACED".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);

            // Put the items back in stock
            for (OrderItem oi : order.getItems()) {
                productRepository.findById(oi.getProductId()).ifPresent(p -> {
                    p.setStock(p.getStock() + oi.getQuantity());
                    productRepository.save(p);
                });
            }
            redirect.addFlashAttribute("success", "Your order has been cancelled.");
        }
        return "redirect:/orders/" + id;
    }
}
