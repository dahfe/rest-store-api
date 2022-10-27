package com.example.springrestapi.rest;

import com.example.springrestapi.dto.UpdatePasswordDTO;
import com.example.springrestapi.dto.UserDTO;
import com.example.springrestapi.model.User;
import com.example.springrestapi.servise.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;


@RestController
@RequestMapping(value = "/api/users")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(name = "id") Long id){
        User user = userService.findById(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDTO userDTO = UserDTO.fromUser(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/username/{name}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable(name = "name") String name){
        User user = userService.findByUsername(name);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDTO userDTO = UserDTO.fromUser(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, Principal principal){
        User user = userService.updateUser(userDTO.toUser(), principal);
        UserDTO newUserDTO = UserDTO.fromUser(user);
        return new ResponseEntity<>(newUserDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/updatePass")
    public ResponseEntity<UserDTO> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, Principal principal){
        User user = userService.updatePassword(principal,
                updatePasswordDTO.getNewPassword(), updatePasswordDTO.getOldPassword());
        UserDTO userDTO = UserDTO.fromUser(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/username")
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable(name = "id") Long id, Principal principal){
        User user = userService.findById(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        userService.delete(id, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/addImg")
    public ResponseEntity<?> addImg(@RequestParam("avatar") MultipartFile file1, Principal principal) throws Exception {
        userService.addImage(principal, file1);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deleteImg")
    public ResponseEntity<?> deleteImg(Principal principal) {
        userService.deleteImage(principal);
        return new ResponseEntity(HttpStatus.OK);
    }
}
