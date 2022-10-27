package com.example.springrestapi.servise;

import com.example.springrestapi.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ProductService {
    List<Product> listProducts(String title);
    Product saveProduct(Principal principal, Product product) throws IOException;
    void deleteProduct(Long id, Principal principal);
    Product getProductById(Long id);
    Product updateProduct(Product newProduct, Principal principal, Long id);
    List<Product> getAll();
    void addImages(Principal principal, List<MultipartFile> files, Long id) throws IOException;
    void deleteImages(Principal principal, Long id);
    List<Product> getProductByUsername(String username);



}
