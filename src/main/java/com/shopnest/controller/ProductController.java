package com.shopnest.controller;

import com.shopnest.model.Product;
import com.shopnest.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public String products(@RequestParam(required = false) String category,
                           @RequestParam(required = false) String q,
                           Model model) {
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasKeyword = q != null && !q.isBlank();

        List<Product> products;
        if (hasCategory && hasKeyword) {
            products = productRepository.findByCategoryAndNameContainingIgnoreCase(category, q.trim());
        } else if (hasCategory) {
            products = productRepository.findByCategory(category);
        } else if (hasKeyword) {
            products = productRepository.findByNameContainingIgnoreCase(q.trim());
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productRepository.findAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", q);
        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("related", productRepository.findTop4ByCategoryAndIdNot(product.getCategory(), id));
        return "product-detail";
    }
}
