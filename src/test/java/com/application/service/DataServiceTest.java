package com.application.service;

import com.application.dto.CityDto;
import com.application.dto.StateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

    @InjectMocks
    DataService dataService= new DataServiceImpl();

    @DisplayName("Test Case - Get States")
    @Test
    void getStates(){
        List<StateDto> states= dataService.getStates("India");
        assertEquals(36, states.size());
    }

    @DisplayName("Test Case - Get States2")
    @Test
    void getStates2(){
        List<StateDto> states= dataService.getStates("India2");
        assertEquals(0, states.size());
    }

    @DisplayName("Test Case - Get Cities")
    @Test
    void getCities(){
        List<CityDto> cities= dataService.getCities("Maharashtra");
        assertEquals(574, cities.size());
    }

    @DisplayName("Test Case - Get Cities2")
    @Test
    void getCities2(){
        List<CityDto> cities= dataService.getCities("Maharashtra2");
        assertEquals(0, cities.size());
    }
}
