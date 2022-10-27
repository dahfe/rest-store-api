package com.example.springrestapi.exception;

import liquibase.pro.packaged.X;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
