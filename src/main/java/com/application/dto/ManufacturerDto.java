package com.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManufacturerDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private static final long serialVersionUID = 4625872131587365313L;

    private Long manufacturerId;

    private String manufacturerCompanyName;

    private String companyEmailAddress;

    private Date dateOfReg;

    private String regtdAt;

    private String phoneNumber;

    private String companyGSTIN;

    private String street;

    private String city;

    private String address;

    private String state;

    private String country;

    @JsonIgnore
    private String token;

    @JsonIgnore
    private String privilegeCode;

    @JsonIgnore
    private boolean accountVerified;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean enabled;

    @JsonIgnore
    private boolean tokenExpired;

}
