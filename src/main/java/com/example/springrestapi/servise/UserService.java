package com.example.springrestapi.servise;

import com.example.springrestapi.model.Role;
import com.example.springrestapi.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface UserService {
    User register(User user);
    List<User> getAll();
    User findByUsername(String name);
    User findById(Long id);
    void delete(Long id, Principal principal);
    void banUser(Long id);
    User updateUser(User newUser, Principal principal);
    User updatePassword(Principal principal, String newPassword, String oldPassword);
    User addImage(Principal principal, MultipartFile file) throws IOException;
    void deleteImage(Principal principal);

}
