package com.project.gengshop.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ProductDto {

    @NotBlank(message = "Product name must not be blank")
    private String name;

    @Size(max = 250, message = "Description must not exceed 250 characters")
    private String description;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Price must not be null")
    @Min(value=0, message = "Quantity must be at least 0")
    private Integer quantity;

    private Long categoryId;
}
