package com.project.gengshop.service;

import com.project.gengshop.dto.CategoryDto;
import com.project.gengshop.exception.ResourceNotFoundException;
import com.project.gengshop.model.Category;
import com.project.gengshop.model.Product;
import com.project.gengshop.repository.CategoryRepository;
import com.project.gengshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    //helper method convert CategoryEntity to CategoryDto
    private CategoryDto convertToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    //helper method convert CategoryDto to CategoryEntity
    private Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    //create category
    public CategoryDto createCategory(CategoryDto categoryDto) {
        //check if category name already exists
        if( categoryRepository.existsByName(categoryDto.getName())) {
            throw new ResourceNotFoundException("Category with name" + categoryDto.getName() + " already exists");
        }

        //convert categoryDto to categoryEntity
        Category newCategory = convertToEntity(categoryDto);
        //saved categoryEntity
        Category savedCategory = categoryRepository.save(newCategory);
        //convert back to categoryDto
        return convertToDto(savedCategory);
    }

    //get all categories
    public List<CategoryDto> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .map(this::convertToDto)
                .toList();
    }

    // get category by id
    public CategoryDto getCategoryById(Long id) {
        Category getCategoryId = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        return convertToDto(getCategoryId);
    }

    //update category by id
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        existingCategory.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return convertToDto(updatedCategory);
    }

    //soft delete category
    @Transactional
    public void deleteCategory(Long id) {
        Category category  = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        //set flag deleted ke true
        category.setDeleted(true);
        categoryRepository.save(category);

        List<Product> productsInCategory = productRepository.findByCategoryId(id);

        for (Product product : productsInCategory) {
            product.setDeleted(true);
        }

        productRepository.saveAll(productsInCategory);
    }

}
