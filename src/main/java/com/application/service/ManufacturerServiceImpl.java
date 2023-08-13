package com.application.service;

import com.application.config.Constants;
import com.application.dto.ManufacturerDto;
import com.application.entity.ManufacturerEntity;
import com.application.exception.ServerException;
import com.application.mapper.ObjectUtils;
import com.application.repository.ManufacturerRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepo manufacturerRepo;

    @Autowired
    private DataService dataService;

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
    public List<ManufacturerDto> createOrUpdateData(ManufacturerDto manufacturerDto) {
        if (dataService.isEmptyOrNull(manufacturerDto.getManufacturerCompanyName())) {
            throw new ServerException("Pass Valid Company Name", Constants.INTERNAL_SERVER,
                    "/create-update", "createOrUpdateData()");
        } else {
            List<ManufacturerDto> manufacturerDtos = getByManufacturerName(manufacturerDto.getManufacturerCompanyName());
            if (manufacturerDtos.isEmpty()) {
                ManufacturerEntity manufacturerEntity = (ManufacturerEntity) ObjectUtils.map(manufacturerDto, new ManufacturerEntity());
                manufacturerRepo.save(manufacturerEntity);
                return getAll();
            } else {
                throw new ServerException("Company Name is already present in database", Constants.INTERNAL_SERVER,
                        "/create-update", "createOrUpdateData()");
            }
        }
    }

}
