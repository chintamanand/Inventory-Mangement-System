package com.application.controller;

import com.application.dto.StockPurchaseDto;
import com.application.service.StockPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/stock")
@RequiredArgsConstructor
public class StockPurchaseController {

    private final StockPurchaseService stockPurchaseService;

    @PostMapping(path = "/placeOrder")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public StockPurchaseDto placeOrder(@RequestBody StockPurchaseDto stockPurchaseDto, HttpServletRequest request) {
        return stockPurchaseService.placeOrder(stockPurchaseDto, request);
    }

}
