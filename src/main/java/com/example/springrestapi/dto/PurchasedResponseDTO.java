package com.example.springrestapi.dto;

import com.example.springrestapi.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PurchasedResponseDTO {
    private String username;
    private List<ProductDTO> productList;
}
