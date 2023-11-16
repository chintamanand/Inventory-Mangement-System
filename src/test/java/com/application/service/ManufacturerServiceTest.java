package com.application.service;

import com.application.dto.ManufacturerDto;
import com.application.repository.ManufacturerRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceTest {

    @Mock
    ManufacturerRepo manufacturerRepo;

    @InjectMocks
    ManufacturerService manufacturerService = new ManufacturerServiceImpl(manufacturerRepo);

    @DisplayName("Test Case - Get States")
    @Test
    void getStates(){
        //List<ManufacturerDto> manufacturers= manufacturerService.createOrUpdateData("India", new MultipartFile("/"));
        //assertEquals(36, manufacturers.size());
    }

}
