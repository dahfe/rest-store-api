package com.example.springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseTitleDTO {
    private String title;
    private Integer price;
    private String sellerName;
}
