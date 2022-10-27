package com.example.springrestapi.rest;

import com.example.springrestapi.dto.*;
import com.example.springrestapi.model.Product;
import com.example.springrestapi.servise.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping(value = "title/{title}")
    public ResponseEntity<List<ResponseTitleDTO>> productsByTitle(@PathVariable(name = "title", required = false) String title){
        List<ResponseTitleDTO> result = filingList(productService.listProducts(title));
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(name = "id") Long id){
        Product product = productService.getProductById(id);
        if(product == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        ProductDTO productDTO = ProductDTO.fromProduct(product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseTitleDTO>> getAll(){
        List<ResponseTitleDTO> result = filingList(productService.getAll());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{username}")
    public ResponseEntity<List<ResponseTitleDTO>> productsByUsername(@PathVariable(name = "username") String username){
        List<ResponseTitleDTO> result = filingList(productService.getProductByUsername(username));
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private List<ResponseTitleDTO> filingList(List<Product> products){
        if(products == null){
            return null;
        }
        List<ResponseTitleDTO> result = new ArrayList<>();
        for (Product product : products){
            ResponseTitleDTO responseTitleDTO = new ResponseTitleDTO(product.getTitle(),
                    product.getPrice(), product.getUser().getUsername());
            result.add(responseTitleDTO);
        }
        return result;
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable(name = "id") Long id, Principal principal){
        Product product = productService.getProductById(id);
        if(product == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        productService.deleteProduct(id, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ProductDTO> createProduct (@RequestBody ProductRequestDTO requestDTO, Principal principal) throws IOException {
        Product product = productService.saveProduct(principal, requestDTO.toProduct());
        ProductDTO productDTO = ProductDTO.fromProduct(product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity<ProductDTO> updateUser(@RequestBody ProductRequestDTO requestDTO, Principal principal, @PathVariable(name = "id") Long id){
        Product product = productService.updateProduct(requestDTO.toProduct(), principal, id);
        ProductDTO newProductDTO = ProductDTO.fromProduct(product);
        return new ResponseEntity<>(newProductDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}/addImg")
    public ResponseEntity<?> addImg(@RequestParam("file1") MultipartFile file1,
                                    @RequestParam("file2") MultipartFile file2,
                                    @RequestParam("file3") MultipartFile file3,
                                    @PathVariable(name = "id") Long id, Principal principal) throws Exception {
        List<MultipartFile> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);
        files.add(file3);
        productService.addImages(principal, files, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{id}/deleteImg")
    public ResponseEntity<?> deleteImg(@PathVariable(name = "id") Long id, Principal principal) {
        productService.deleteImages(principal, id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
