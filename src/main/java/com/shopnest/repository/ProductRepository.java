package com.shopnest.repository;

import com.shopnest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategoryAndNameContainingIgnoreCase(String category, String keyword);

    List<Product> findTop8ByOrderByIdDesc();

    List<Product> findTop4ByCategoryAndIdNot(String category, Long id);

    @Query("select distinct p.category from Product p order by p.category")
    List<String> findAllCategories();
}
