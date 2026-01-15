package com.project.gengshop.controller;

import com.project.gengshop.dto.ProductDto;
import com.project.gengshop.repository.ProductRepository;
import com.project.gengshop.service.CategoryService;
import com.project.gengshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    //create single product
    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProducts(@Valid @RequestBody ProductDto productDto) {
        ProductDto createdProducts = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProducts);

    }

}
