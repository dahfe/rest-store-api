package com.example.springrestapi.dto;

import com.example.springrestapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import liquibase.pro.packaged.S;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequestDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;


    public User toUser(){
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    public static RegisterRequestDTO fromUser(User user) {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setUsername(user.getUsername());
        requestDTO.setFirstName(user.getFirstName());
        requestDTO.setLastName(user.getLastName());
        requestDTO.setEmail(user.getEmail());
        requestDTO.setPassword(user.getPassword());

        return requestDTO;
    }
}
