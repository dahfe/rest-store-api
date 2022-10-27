package com.example.springrestapi.repository;

import com.example.springrestapi.model.Product;
import com.example.springrestapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitle(String title);
    List<Product> findByUserId(Long userId);
}
