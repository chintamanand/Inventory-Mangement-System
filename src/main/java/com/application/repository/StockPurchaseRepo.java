package com.application.repository;

import com.application.entity.ManufacturerEntity;
import com.application.entity.StockPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPurchaseRepo extends JpaRepository<StockPurchaseEntity, Long> {

    List<ManufacturerEntity> findByProductName(String productName);

}
