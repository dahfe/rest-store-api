package com.example.springrestapi.servise.impl;

import com.example.springrestapi.exception.ProductNotFoundException;
import com.example.springrestapi.model.Image;
import com.example.springrestapi.model.Product;
import com.example.springrestapi.model.User;
import com.example.springrestapi.payload.ServicePayload;
import com.example.springrestapi.repository.ProductRepository;
import com.example.springrestapi.repository.UserRepository;
import com.example.springrestapi.servise.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    @Override
    public List<Product> listProducts(String title) {
        if(title != null){
            List<Product> products = productRepository.findByTitle(title.trim());
            if(products == null){
                log.info("{} found nothing", title);
            }
            return products;
        }
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = productRepository.findAll();
        log.info("{} users successfully find", products.size());
        return products;
    }

    @Override
    public Product saveProduct(Principal principal, Product product) {
        product.setUser(userRepository.findByUsername(principal.getName()));
        product.setCreated(new Date());
        product.setUpdated(new Date());
        log.info("Saving new Product. Title: {}; Author username: {}", product.getTitle(), product.getUser().getUsername());
        return productRepository.save(product);
    }

    @Override
    public void addImages(Principal principal, List<MultipartFile> files, Long id) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("product not found"));
        User user = userRepository.findByUsername(principal.getName());

        if(!product.getUser().getId().equals(user.getId())){
            throw new ProductNotFoundException("You cannot add image to product other users");
        }
        Image image;
        for(MultipartFile file : files){
            if(file.getSize() != 0) {
                image = ServicePayload.toImageEntity(file);
                product.addImageToProduct(image);
            }
        }
        product.setUpdated(new Date());
        Product productFromDB = productRepository.save(product);
        productFromDB.setPreviewImageId(productFromDB.getImages().get(0).getId());
        productFromDB.getImages().get(0).setPreviewImage(true);
        productRepository.save(productFromDB);
    }

    @Override
    public List<Product> getProductByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("user not found");
        }
        List<Product> products = productRepository.findByUserId(user.getId());
        log.info("{} products by {} username successfully find", products.size(), user.getUsername());
        return products;
    }

    @Override
    public void deleteProduct(Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("product not found"));
        List<String> roles = ServicePayload.getRoleNames(user.getRoles());
        for (String role : roles){
            if(!role.equals("ROLE_ADMIN") || !user.getId().equals(product.getUser().getId())){
                throw new ProductNotFoundException("You cannot delete product other users");
            }
        }

        productRepository.deleteById(id);
        log.info("product with id {} successfully deleted", id);
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            log.warn("product with id {} doesn't find", id);
            return null;
        }
        log.info("product {} successfully find", product);
        return product;
    }

    @Override
    public Product updateProduct(Product newProduct, Principal principal, Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("product not found"));
        if(!product.getUser().getUsername().equals(principal.getName())){
            throw new ProductNotFoundException("product not found");
        }
        product.setTitle(newProduct.getTitle());
        product.setDescription(newProduct.getDescription());
        product.setUpdated(new Date());
        product.setPrice(newProduct.getPrice());
        product.setCity(newProduct.getCity());
        productRepository.save(product);

        return product;
    }

    @Override
    public void deleteImages(Principal principal, Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("product not found"));
        User user = userRepository.findByUsername(principal.getName());
        if(!product.getUser().getId().equals(user.getId())){
            throw new ProductNotFoundException("You cannot add image to product other users");
        }
        product.deleteImagesFromProduct(product.getImages());
        product.setPreviewImageId(null);
        product.setUpdated(new Date());
        productRepository.save(product);
    }
}
