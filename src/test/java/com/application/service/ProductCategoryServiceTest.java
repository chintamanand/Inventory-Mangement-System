package com.application.service;

import com.application.dto.ProductCategoryDto;
import com.application.dto.StateDto;
import com.application.repository.ProductCategoryRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @Mock
    ProductCategoryRepo repo;

    @InjectMocks
    ProductCategoryService productCategoryService = new ProductCategoryServiceImpl(repo);

    @DisplayName("Test Case - Get Product Categories")
    @Test
    void getProductCategories(){
        List<ProductCategoryDto> productCategories= productCategoryService.getProductCategories();
        assertEquals(10, productCategories.size());
    }
}
