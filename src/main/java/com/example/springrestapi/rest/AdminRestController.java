package com.example.springrestapi.rest;

import com.example.springrestapi.dto.AdminUserDTO;
import com.example.springrestapi.dto.UserDTO;
import com.example.springrestapi.model.User;
import com.example.springrestapi.servise.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminRestController {

    private final UserService userService;


    @GetMapping(value = "/user/{id}")
    public ResponseEntity<AdminUserDTO> getUserById(@PathVariable(name = "id") Long id){
        User user = userService.findById(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        AdminUserDTO adminUserDTO = AdminUserDTO.fromUser(user);
        return new ResponseEntity<>(adminUserDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(){
        List<AdminUserDTO> adminUserDTOList = new ArrayList<>();
        for(User user : userService.getAll()){
            adminUserDTOList.add(AdminUserDTO.fromUser(user));
        }
        return new ResponseEntity<>(adminUserDTOList, HttpStatus.OK);
    }

    @PostMapping(value = "user/ban/{id}")
    public ResponseEntity banUser(@PathVariable(name = "id") Long id){
        User user = userService.findById(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        userService.banUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
