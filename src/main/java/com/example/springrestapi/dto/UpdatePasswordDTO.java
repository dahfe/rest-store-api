package com.example.springrestapi.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {

    private String newPassword;
    private String oldPassword;
}
