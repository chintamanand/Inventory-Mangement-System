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
import java.util.Date;
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
    public List<ProductDto> createOrUpdateData(ProductDto productRequest, HttpServletRequest request) throws BusinessGlobalException {
        log.info("Product info received :: " + productRequest.toString());
        ManufacturerDto manufacturerDto;
        if (productRequest.getManufacturerId() == null) {
            throw new ServerException("Invalid Manufacturer Id", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), "Product Create Service");
        }
        manufacturerDto = manufacturerService.getByManufacturerId(productRequest.getManufacturerId());
        if (manufacturerDto == null) {
            throw new ServerException("Given ManufacturerId Not Found", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), "Product Create Service");
        }

        List<ProductEntity> entities = productRepo.findByManufacturerNameAndProductName(manufacturerDto.getManufacturerCompanyName(), productRequest.getProductName());
        List<ProductDto> productDtoList = entities.stream().map(
                        productEntity1 -> (ProductDto) ObjectUtils.map(productEntity1, new ProductDto()))
                .collect(Collectors.toList());

        ProductEntity productEntity = null;
        if (!productDtoList.isEmpty()) {
            for (ProductDto product1 : productDtoList) {
                ProductDto product = new ProductDto();
                ObjectUtils.map(productRequest, product);
                product.setProductId(product1.getProductId());
                product.setCreatedOn(product1.getCreatedOn());
                product.setManufacturerName(manufacturerDto.getManufacturerCompanyName());
                product.setTotalCost(productRequest.getLandedCost() + productRequest.getUnitCost());
                product.setTotalWeightOfUnits(productRequest.getWeightOfUnit() * productRequest.getNoOfUnits());
                product.setTotalProductValue(product.getTotalCost() * product.getTotalWeightOfUnits());
                product.setEnabled(true);
                product.setLastUpdated(new Date());
                productEntity = (ProductEntity) ObjectUtils.map(product, new ProductEntity());
                productRepo.save(productEntity);
            }
        } else {
            productRequest.setManufacturerName(manufacturerDto.getManufacturerCompanyName());
            productRequest.setTotalCost(productRequest.getLandedCost() + productRequest.getUnitCost());
            productRequest.setTotalWeightOfUnits(productRequest.getWeightOfUnit() * productRequest.getNoOfUnits());
            productRequest.setTotalProductValue(productRequest.getTotalCost() * productRequest.getTotalWeightOfUnits());
            productRequest.setEnabled(true);
            productRequest.setLastUpdated(new Date());
            productRequest.setCreatedOn(new Date());
            productEntity = (ProductEntity) ObjectUtils.map(productRequest, new ProductEntity());
            productRepo.save(productEntity);
        }

        return getAll();
    }

    @Override
    public List<ProductDto> deleteIdById(String id) {
        if (id == null || id.isEmpty()) {
            throw new ServerException("Invalid Product Id was passed", Constants.INTERNAL_SERVER,
                    "/product/delete", "deleteIdById()");
        }
        productRepo.deleteById(Long.valueOf(id));
        return getAll();
    }

}
