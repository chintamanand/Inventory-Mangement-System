package com.application.service;

import com.application.dto.StockPurchaseDto;

import javax.servlet.http.HttpServletRequest;

public interface StockPurchaseService {
    StockPurchaseDto placeOrder(StockPurchaseDto stockPurchaseDto, HttpServletRequest request);

}
