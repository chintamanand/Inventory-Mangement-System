package com.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {

    @JsonProperty("city_id")
    public long cityId;

    @JsonProperty("city_name")
    public String cityName;

}

