package com.application.repository;

import com.application.entity.ManufacturerEntity;
import com.application.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long> {

    List<ManufacturerEntity> findByProductName(String productName);

}
