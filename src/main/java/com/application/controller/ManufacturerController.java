package com.application.controller;

import com.application.dto.ManufacturerDto;
import com.application.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/manufacturer")
@RequiredArgsConstructor
@Log4j2
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @GetMapping(path = "/get")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ManufacturerDto> getAll(HttpServletRequest request) {
        log.info("Entered Manufacturer GetAll Method");
        log.info(request.toString());
        return manufacturerService.getAll();
    }

    @GetMapping(path = "/get/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ManufacturerDto getById(@PathVariable long id) {
        return manufacturerService.getByManufacturerId(id);
    }

    @GetMapping(path = "/getByName")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ManufacturerDto> getByName(@RequestParam("manufacturerName") String name){
        return manufacturerService.getByManufacturerName(name);
    }

    @PostMapping(path = "/create-update")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ManufacturerDto> createOrUpdateData(@RequestBody ManufacturerDto manufacturerDto) {
        return manufacturerService.createOrUpdateData(manufacturerDto);
    }

}
