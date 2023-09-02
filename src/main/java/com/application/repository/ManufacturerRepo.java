package com.application.repository;

import com.application.entity.ManufacturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ManufacturerRepo extends JpaRepository<ManufacturerEntity, Long> {

    List<ManufacturerEntity> findByManufacturerCompanyName(String manufacturerName);

    @Query(value = "SELECT COUNT(*) FROM manufacturer m where m.created_on BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long getRecentlyAdded(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
