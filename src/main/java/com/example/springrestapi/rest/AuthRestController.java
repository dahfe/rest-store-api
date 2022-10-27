package com.example.springrestapi.rest;

import com.example.springrestapi.dto.*;
import com.example.springrestapi.model.User;
import com.example.springrestapi.security.jwt.JwtTokenProvider;
import com.example.springrestapi.servise.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO){
        try {
            String username = authRequestDTO.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authRequestDTO.getPassword()));
            User user = userService.findByUsername(username);
            if(user == null){
                throw new UsernameNotFoundException("User with username " + username + " not found");
            }
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, token);
            return ResponseEntity.ok(authResponseDTO);
        }
        catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody RegisterRequestDTO requestDTO) {
        User user = userService.register(requestDTO.toUser());
        RegisterResponseDTO responseDTO = new RegisterResponseDTO(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
