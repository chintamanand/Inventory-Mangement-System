package com.application.service;

import com.application.config.Constants;
import com.application.dto.ManufacturerDto;
import com.application.dto.ProductDto;
import com.application.entity.ProductEntity;
import com.application.exception.BusinessGlobalException;
import com.application.exception.ServerException;
import com.application.mapper.ObjectUtils;
import com.application.repository.ProductRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    private final ManufacturerService manufacturerService;

    public ProductServiceImpl(ProductRepo productRepo, final ManufacturerService manufacturerService) {
        this.productRepo = productRepo;
        this.manufacturerService = manufacturerService;
    }

    @Override
    public List<ProductDto> getAll() {
        List<ProductEntity> entities = productRepo.findAll();
        return entities.stream().map(
                        productEntity -> (ProductDto) ObjectUtils.map(productEntity, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getByProductId(long productId) {
        return productRepo.findById(productId).map(
                        productEntity -> (ProductDto) ObjectUtils.map(productEntity, new ProductDto()))
                .orElse(null);
    }

    @Override
    public List<ProductDto> getByProductName(String productName) {
        return productRepo.findByProductName(productName).stream().map(
                        productEntity -> (ProductDto) ObjectUtils.map(productEntity, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> createOrUpdateData(ProductDto productDto, HttpServletRequest request) throws BusinessGlobalException {
        log.info("Product info received -- " + productDto.toString());
        ManufacturerDto manufacturerDto;
        if (productDto.getManufacturerId() == null) {
            throw new ServerException("Invalid Manufacturer Id", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), "Product Create Service");
        }
        manufacturerDto = manufacturerService.getByManufacturerId(productDto.getManufacturerId());
        if (manufacturerDto == null) {
            throw new ServerException("Given ManufacturerId Not Found", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), "Product Create Service");
        }

        //if we get the same product twice then we need to update it into single entry only
        productDto.setManufacturerName(manufacturerDto.getManufacturerCompanyName());
        productDto.setTotalCost(productDto.getLandedCost() + productDto.getUnitCost());
        productDto.setTotalWeightOfUnits(productDto.getWeightOfUnit() * productDto.getNoOfUnits());
        productDto.setTotalProductValue(productDto.getTotalCost() * productDto.getTotalWeightOfUnits());
        ProductEntity productEntity = (ProductEntity) ObjectUtils.map(productDto, new ProductEntity());
        productRepo.save(productEntity);
        return getAll();
    }

}
