package com.example.springrestapi.rest;

import com.example.springrestapi.exception.ImageNotFoundException;
import com.example.springrestapi.exception.UserNotFoundException;
import com.example.springrestapi.model.Image;
import com.example.springrestapi.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("api/image")
@AllArgsConstructor
public class ImageRestController {

    private final ImageRepository imageRepository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getImageById(@PathVariable(name = "id") Long id){
        Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("image not found"));
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
