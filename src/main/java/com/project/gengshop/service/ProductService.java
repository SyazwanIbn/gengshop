package com.project.gengshop.service;

import com.project.gengshop.dto.ProductDto;
import com.project.gengshop.exception.ResourceNotFoundException;
import com.project.gengshop.model.Category;
import com.project.gengshop.model.Product;
import com.project.gengshop.repository.CategoryRepository;
import com.project.gengshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    //convert ProductEntity to ProductDto
    private ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setCategoryId(product.getCategory().getId());
        return productDto;
    }

    //convert ProductDto to ProductEntity
    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Category with id " + productDto.getCategoryId() + " not found"
                ));
        product.setCategory(category);
        return product;
    }

    //create product
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        //check if product exist by name
        if (productRepository.existsByName(productDto.getName())) {
            throw new ResourceNotFoundException("Product with name " + productDto.getName() + " already exists");
        }
        //convert dto to entity
        Product newProduct = convertToEntity(productDto);
        //save new Product
        Product savedProduct = productRepository.save(newProduct);
        return convertToDto(savedProduct);
    }
}
