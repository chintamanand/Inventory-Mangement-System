package com.application.service;

import com.application.dto.ManufacturerDto;
import com.application.entity.ManufacturerEntity;
import com.application.mapper.ObjectUtils;
import com.application.repository.ManufacturerRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepo manufacturerRepo;

    public ManufacturerServiceImpl(ManufacturerRepo manufacturerRepo) {
        this.manufacturerRepo = manufacturerRepo;
    }

    @Override
    public List<ManufacturerDto> getAll() {
        List<ManufacturerEntity> entities = manufacturerRepo.findAll();

        return entities.stream().map(
                manufacturerEntity -> (ManufacturerDto) ObjectUtils.map(manufacturerEntity, new ManufacturerDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ManufacturerDto getByManufacturerId(long manufacturerId) {
        return manufacturerRepo.findById(manufacturerId).map(
                manufacturerEntity -> (ManufacturerDto) ObjectUtils.map(manufacturerEntity, new ManufacturerDto()))
                .orElse(null);
    }

    @Override
    public List<ManufacturerDto> getByManufacturerName(String manufacturerName) {
        return manufacturerRepo.findByManufacturerCompanyName(manufacturerName).stream().map(
                manufacturerEntity -> (ManufacturerDto) ObjectUtils.map(manufacturerEntity, new ManufacturerDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ManufacturerDto createOrUpdateData(ManufacturerDto manufacturerDto) {
        ManufacturerEntity manufacturerEntity = (ManufacturerEntity) ObjectUtils.map(manufacturerDto, new ManufacturerEntity());
        manufacturerEntity = manufacturerRepo.save(manufacturerEntity);
        return (ManufacturerDto) ObjectUtils.map(manufacturerEntity, new ManufacturerDto());
    }

}
