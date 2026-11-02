package com.shopnest.service;

import com.shopnest.model.CartItem;
import com.shopnest.model.User;
import com.shopnest.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    public static final double FREE_DELIVERY_ABOVE = 499;
    public static final double DELIVERY_CHARGE = 40;

    private final CartItemRepository cartItemRepository;

    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> getItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public double subtotal(List<CartItem> items) {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.getLineTotal();
        }
        return sum;
    }

    public double delivery(double subtotal) {
        if (subtotal == 0 || subtotal >= FREE_DELIVERY_ABOVE) return 0;
        return DELIVERY_CHARGE;
    }
}
