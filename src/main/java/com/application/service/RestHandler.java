package com.application.service;

import com.application.dto.CityDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Log4j2
@Configuration
public class RestHandler {

    private final RestTemplate restTemplate;
    //https://www.universal-tutorial.com/rest-apis/free-rest-api-for-country-state-city

    @Value(value = "${external.api1.token}")
    private String apiToken1;

    @Value("${external.api1.url}")
    private String apiUrl1;

    public RestHandler(){
        this.restTemplate = new RestTemplate();
    }

    public List<CityDto> getStates(String countryName) {
        log.info("Entered the RestTemplate's getState Method -- "+ countryName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + apiToken1);
        headers.set("user-email", "chintamanand56@gmail.com");
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        log.info("api Url: " + apiUrl1+"states/"+ countryName);
        log.info("api token: " + apiToken1);
        log.info("headers Info: " + headers.toString());
        ResponseEntity<String> response = new RestTemplate().exchange(apiUrl1+"states/"+ countryName, HttpMethod.GET, httpEntity, String.class);
        log.info("Response received: "+response.getBody());

        return Collections.emptyList();
    }

}
