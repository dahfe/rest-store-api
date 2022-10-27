package com.example.springrestapi.dto;

import com.example.springrestapi.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDTO {
    private String title;
    private String description;
    private Integer price;
    private String city;


    public Product toProduct(){
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setCity(city);

        return product;
    }

    public static ProductDTO fromProduct(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle(product.getTitle());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setCity(product.getCity());

        return productDTO;
    }
}
