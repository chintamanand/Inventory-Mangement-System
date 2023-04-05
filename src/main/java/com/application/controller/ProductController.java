package com.application.controller;

import com.application.dto.ProductDto;
import com.application.exception.BusinessGlobalException;
import com.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(path = "/get")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ProductDto> getAll() {
        return productService.getAll();
    }

    @GetMapping(path = "/get/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ProductDto getById(@PathVariable long id) {
        return productService.getByProductId(id);
    }

    @GetMapping(path = "/getByName")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ProductDto> getByName(@RequestParam("productName") String productName){
        return productService.getByProductName(productName);
    }

    @PostMapping(path = "/create-update")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ProductDto createOrUpdateData(@RequestBody ProductDto productDto,
                                         HttpServletRequest request) throws BusinessGlobalException {
        return productService.createOrUpdateData(productDto, request);
    }

}
