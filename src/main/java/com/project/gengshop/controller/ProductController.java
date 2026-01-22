package com.project.gengshop.controller;

import com.project.gengshop.dto.ProductDto;
import com.project.gengshop.service.CategoryService;
import com.project.gengshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    //get all products
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> getALLProducts = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(getALLProducts);
    }

    //get product by id
    @GetMapping("products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }

    //update Product by id
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @Valid
            @PathVariable Long id,
            @RequestBody ProductDto productDto) {
        ProductDto updatedProducts = productService.updateProduct(id, productDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProducts);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product with ID " + id + " has been deleted");
    }

}
