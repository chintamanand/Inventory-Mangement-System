package com.application.service;

import com.application.config.Constants;
import com.application.dto.CityDto;
import com.application.dto.ManufacturerDto;
import com.application.dto.ProductDto;
import com.application.dto.StateDto;
import com.application.exception.BusinessGlobalException;
import com.application.exception.ServerException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DataServiceImpl implements DataService {

    private final ManufacturerService manufacturerService;
    private final ProductService productService;
    Comparator<StateDto> compareByStateName = Comparator.comparing(StateDto::getStateName);
    private List<StateDto> masterStateInfo = new ArrayList<>();
    private List<StateDto> indviStateDetails = new ArrayList<>();

    public DataServiceImpl(ManufacturerService manufacturerService, ProductService productService) {
        this.manufacturerService = manufacturerService;
        this.productService = productService;
    }

    @Override
    public List<StateDto> getStates(String countryName) {
        log.info("Entered the getStates Method - " + countryName);
        if (masterStateInfo.isEmpty()) {
            masterStateInfo = parseCsvFile();
            return sortStateList();
        } else {
            if (indviStateDetails.isEmpty()) {
                return sortStateList();
            } else {
                return indviStateDetails;
            }
        }
    }

    private List<StateDto> parseCsvFile() {
        Resource resource = new ClassPathResource("cities.csv");
        try {
            CSVParser csvParser = new CSVParser(new FileReader(resource.getFile()), CSVFormat.DEFAULT
                    .withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                if (csvRecord.get("country_code").equalsIgnoreCase("IN") &&
                        csvRecord.get("country_name").equalsIgnoreCase("India")) {
                    StateDto stateDto = new StateDto(Long.parseLong(csvRecord.get("Id")), csvRecord.get("name"),
                            Long.parseLong(csvRecord.get("state_id")), csvRecord.get("state_code"), csvRecord.get("state_name"),
                            Long.parseLong(csvRecord.get("country_id")), csvRecord.get("country_code"), csvRecord.get("country_name"),
                            csvRecord.get("latitude"), csvRecord.get("longitude"));
                    masterStateInfo.add(stateDto);
                }
            }
        } catch (Exception e) {
            log.info("Error has occurred while parsing the CSV file");
        }
        return masterStateInfo;
    }

    private List<StateDto> sortStateList() {
        Map<String, StateDto> stateSet = new HashMap<>();
        for (StateDto stateDto : masterStateInfo) {
            if (!stateDto.getStateName().isEmpty()) {
                stateSet.put(stateDto.getStateName(), stateDto);
            }
        }
        indviStateDetails = new ArrayList<>(stateSet.values());
        indviStateDetails.sort(compareByStateName);
        return indviStateDetails;
    }

    @Override
    public List<CityDto> getCities(String stateName) {
        log.info("Entered the getCities method -- " + stateName);
        List<CityDto> cities = new ArrayList<>();
        if (masterStateInfo.isEmpty()) {
            masterStateInfo = getStates("India");
        }

        List<StateDto> stateList = masterStateInfo.stream()
                .filter(stateDto -> !stateDto.getStateName().isEmpty()
                        && stateDto.getStateName().equalsIgnoreCase(stateName))
                .collect(Collectors.toList());

        for (StateDto state : stateList) {
            cities.add(new CityDto(state.getCityId(), state.getCityName()));
        }

        log.info("Leaving the getCities method");
        return cities;
    }

    @Override
    public String getExternalCountryDetails() {
        RestTemplate restTemplate = new RestTemplate();

        String accessToken = getExternalAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        String s = restTemplate.postForObject("https://www.universal-tutorial.com/api/countries", httpEntity, String.class);

        return s;
    }

    private String getExternalAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-token", "2co4c39Yz9SwTBorTtHO3qSNjGjrw08P1bZztMAL4kgIjGRqZk9LfZkhW_41rzVpgt0");
        httpHeaders.set("user-email", "chintamanand56@gmail.com");

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        String s = restTemplate.postForObject("https://www.universal-tutorial.com/api/getaccesstoken", httpEntity, String.class);

        return s;
    }

    @Override
    public ResponseEntity<Resource> generateXcelSheet(String dataType, HttpServletRequest request)
            throws BusinessGlobalException {
        if (dataType == null || dataType.isEmpty()) {
            throw new ServerException("Input DataType is Invalid", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), "generateXcelSheet Service");
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet(dataType);
        List<ManufacturerDto> manufacturerData;
        List<ProductDto> productData;

        if (dataType.equalsIgnoreCase("manufacturer")) {
            manufacturerData = manufacturerService.getAll();
            setXcelHeaderRow(ManufacturerDto.class, spreadsheet);
            setXcelData(ManufacturerDto.class, manufacturerData.size(), spreadsheet, Arrays.asList(manufacturerData));
        } else if (dataType.equalsIgnoreCase("product")) {
            productData = productService.getAll();
            setXcelHeaderRow(ProductDto.class, spreadsheet);
            setXcelData(ProductDto.class, productData.size(), spreadsheet, Arrays.asList(productData));
        } else if (dataType.equalsIgnoreCase("transaction")) {
            // transaction
        } else {
            throw new ServerException("Input DataType is Invalid", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), "generateXcelSheet Service");
        }

        try {
            String fileName = dataType + "_data.xlsx";
            File file = new File(fileName);

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();

            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            log.info(fileName + " written successfully on disk.");

            ResponseEntity<Resource> responseEntity = ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);

            if (file.exists()) {
                file.delete();
            }

            return responseEntity;
        } catch (Exception e) {
            throw new ServerException("File download service failed", Constants.SERVICE_ERROR,
                    request.getRequestURL().toString(), "generateXcelSheet Service");
        }

    }

    private XSSFSheet setXcelHeaderRow(Class c, XSSFSheet spreadsheet) {
        XSSFRow row = spreadsheet.createRow(0);
        int cellCount = 0;
        Field[] fields = c.getDeclaredFields();
        for (int fieldCount = 0; fieldCount < c.getDeclaredFields().length - 1; fieldCount++) {
            Cell cell = row.createCell(cellCount);
            cell.setCellValue(fields[fieldCount + 1].getName());
            cellCount++;
        }
        return spreadsheet;
    }

    private XSSFSheet setXcelData(Class c, int dataSize, XSSFSheet spreadsheet, List<Object> data1) {
        List<Object> data = ((ArrayList) data1.get(0));
        if (data == null || data.isEmpty()) {
            log.info("Data is empty");
        }

        for (int rowNo = 0; rowNo < dataSize; rowNo++) {
            XSSFRow row = spreadsheet.createRow(rowNo + 1);
            for (int cellNo = 0; cellNo < c.getDeclaredFields().length; cellNo++) {
                Cell cell = row.createCell(cellNo, CellType.STRING);

                Field[] fields = c.getDeclaredFields();
                try {
                    if (data.get(rowNo) instanceof ManufacturerDto) {
                        ManufacturerDto manufacturerDto = (ManufacturerDto) data.get(rowNo);
                        Field field = ManufacturerDto.class.getDeclaredField(fields[cellNo + 1].getName());
                        field.setAccessible(true);
                        cell.setCellValue(field.get(manufacturerDto).toString());
                    } else if (data.get(rowNo) instanceof ProductDto) {
                        ProductDto productDto = (ProductDto) data.get(rowNo);
                        Field field = ProductDto.class.getDeclaredField(fields[cellNo + 1].getName());
                        field.setAccessible(true);
                        cell.setCellValue(field.get(productDto).toString());
                    }
                } catch (Exception e) {
                    /*throw new ServerException("File download service failed", Constants.SERVICE_ERROR,
                            "/data/generateXcel", "generateXcelSheet Service");*/
                }

            }

        }

        return spreadsheet;
    }

}
