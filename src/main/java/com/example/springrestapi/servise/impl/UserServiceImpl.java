package com.example.springrestapi.servise.impl;

import com.example.springrestapi.exception.UserExistsException;
import com.example.springrestapi.exception.UserNotFoundException;
import com.example.springrestapi.model.*;
import com.example.springrestapi.payload.ServicePayload;
import com.example.springrestapi.repository.RoleRepository;
import com.example.springrestapi.repository.UserRepository;
import com.example.springrestapi.servise.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;


@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        String username = user.getUsername();
        if(userRepository.findByUsername(username) != null){
            throw new UserExistsException("user already exists");
        }
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        User registeredUser = userRepository.save(user);

        log.info("user {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("{} users successfully find", result.size());
        return result;
    }

    @Override
    public User findByUsername(String name) {
        User result = userRepository.findByUsername(name);
        if(result == null){
            log.warn("user with name {} doesn't find", name);
            return null;
        }
        log.info("user {} successfully find", result);
        return result;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            log.warn("user with id {} doesn't find", id);
            return null;
        }
        log.info("user {} successfully find", user);
        return user;
    }

    @Override
    public void delete(Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        List<String> roles = ServicePayload.getRoleNames(user.getRoles());
        for (String role : roles){
            if(!role.equals("ROLE_ADMIN") || !user.getId().equals(id)){
                throw new UserNotFoundException("You cannot delete other users");
            }
        }
        userRepository.deleteById(id);
        log.info("user with id {} successfully deleted", id);
    }

    @Override
    public void banUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user is not find"));
        if(user != null){
            if(user.getStatus().equals(Status.ACTIVE)){
                user.setStatus(Status.NOT_ACTIVE);
                log.info("Ban user with id {}; username {}", user.getId(), user.getUsername());
            }
            else {
                user.setStatus(Status.ACTIVE);
                log.info("Unban user with id {}; username {}", user.getId(), user.getUsername());
            }
            userRepository.save(user);
        }
    }

    @Override
    public User updateUser(User newUser, Principal principal){
        User user = userRepository.findByUsername(principal.getName());

        user.setUsername(newUser.getUsername());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setUpdated(new Date());
        userRepository.save(user);

        return user;
    }

    @Override
    public User updatePassword(Principal principal, String newPassword, String oldPassword){
        User user = userRepository.findByUsername(principal.getName());
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            log.warn("user with name {} and password {} doesn't find", user.getUsername(), oldPassword);
            return null;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdated(new Date());
        userRepository.save(user);
        return user;
    }

    public User addImage(Principal principal, MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(principal.getName());
        if(file.getSize() != 0){
            Image image = ServicePayload.toImageEntity(file);
            image.setPreviewImage(true);
            user.addImageToUser(image);
        }
        userRepository.save(user);
        return user;
    }

    public void deleteImage(Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        user.deleteImageFromUser();
        user.setUpdated(new Date());
        userRepository.save(user);
    }



}
