package com.application.controller;

import com.application.dto.*;
import com.application.exception.BusinessGlobalException;
import com.application.service.DataService;
import com.application.service.ManufacturerService;
import com.application.service.ProductCategoryService;
import com.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/data")
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    private final ProductCategoryService productCategoryService;

    private final ManufacturerService manufacturerService;

    private final ProductService productService;

    @GetMapping(path = "/getStates")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<StateDto> getStateDetails(@RequestParam("countryName") String countryName) {
        return dataService.getStates(countryName);
    }

    @GetMapping(path = "/getCities")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<CityDto> getCityDetails(@RequestParam("stateName") String stateName) {
        return dataService.getCities(stateName);
    }

    @GetMapping(path = "/generateXcel/{dataType}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Resource> generateXcel(@PathVariable("dataType") String dataType,
                                                 HttpServletRequest request) throws BusinessGlobalException {
        return dataService.generateXcelSheet(dataType, request);
    }

    @GetMapping(path = "/getCategory")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ProductCategoryDto> getCategory() {
        return productCategoryService.getProductCategories();
    }

    @GetMapping(path = "/getOverview")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public OverviewResponse getOverview() {
       return dataService.getOverview();
    }

}
