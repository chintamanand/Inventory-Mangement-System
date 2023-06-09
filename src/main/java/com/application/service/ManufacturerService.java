package com.application.service;

import com.application.dto.ManufacturerDto;

import java.util.List;

public interface ManufacturerService {

    List<ManufacturerDto> getAll();

    ManufacturerDto getByManufacturerId(long manufacturerId);

    List<ManufacturerDto> getByManufacturerName(String manufacturerName);

    ManufacturerDto createOrUpdateData(ManufacturerDto manufacturerDto);
}
