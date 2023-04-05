package com.application.service;

import com.application.config.Constants;
import com.application.dto.ManufacturerDto;
import com.application.dto.ProductDto;
import com.application.dto.StockPurchaseDto;
import com.application.entity.StockPurchaseEntity;
import com.application.exception.ServerException;
import com.application.mapper.ObjectUtils;
import com.application.repository.StockPurchaseRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.application.config.Constants.PLACE_ORDER_METHOD;

@Log4j2
@Service
public class StockPurchaseServiceImpl implements StockPurchaseService {

    private final StockPurchaseRepo stockPurchaseRepo;

    private final ManufacturerService manufacturerService;

    private final ProductService productService;

    public StockPurchaseServiceImpl(StockPurchaseRepo stockPurchaseRepo,
                                    final ManufacturerService manufacturerService,
                                    final ProductService productService) {
        this.stockPurchaseRepo = stockPurchaseRepo;
        this.manufacturerService = manufacturerService;
        this.productService = productService;
    }

    @Override
    public StockPurchaseDto placeOrder(StockPurchaseDto stockPurchaseDto, HttpServletRequest request) {
        if (stockPurchaseDto == null) {
            throw new ServerException("Invalid Request Payload", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), Constants.PLACE_ORDER_METHOD);
        }

        if (stockPurchaseDto.getManufacturerId() == null || stockPurchaseDto.getProductId() == null) {
            throw new ServerException("Invalid ManufacturerId/ProductId", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), Constants.PLACE_ORDER_METHOD);
        }

        log.info("Stock Purchase Order request --- " + stockPurchaseDto.toString());
        ManufacturerDto manufacturerDto = manufacturerService.getByManufacturerId(stockPurchaseDto.getManufacturerId());
        ProductDto productDto = productService.getByProductId(stockPurchaseDto.getProductId());
        if (manufacturerDto == null || productDto == null) {
            throw new ServerException("Unable find entry in Manufacturer/Product", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), Constants.PLACE_ORDER_METHOD);
        }
        stockPurchaseDto.setManufacturerName(manufacturerDto.getManufacturerCompanyName());
        stockPurchaseDto.setProductName(productDto.getProductName());
        stockPurchaseDto.setProductCategoryName(productDto.getProductCategory());
        stockPurchaseDto.setTotalWeight(productDto.getNoOfUnits() * productDto.getWeightOfUnit());
        stockPurchaseDto.setProductValue(productDto.getTotalCost());
        
        StockPurchaseEntity stockPurchaseEntity = (StockPurchaseEntity) ObjectUtils.map(stockPurchaseDto, new StockPurchaseEntity());
        stockPurchaseEntity = stockPurchaseRepo.save(stockPurchaseEntity);
        return (StockPurchaseDto) ObjectUtils.map(stockPurchaseEntity, new StockPurchaseDto());
    }

}
