package com.application.repository;

import com.application.entity.ManufacturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerRepo extends JpaRepository<ManufacturerEntity, Long> {

    List<ManufacturerEntity> findByManufacturerCompanyName(String manufacturerName);

}
