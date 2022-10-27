package com.example.springrestapi.repository;

import com.example.springrestapi.model.Image;
import com.example.springrestapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteAllByProduct(Product product);
}
