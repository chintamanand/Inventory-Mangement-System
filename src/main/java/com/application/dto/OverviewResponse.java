package com.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class OverviewResponse implements Serializable {

    private static final long serialVersionUID = 2222593343104168066L;

    private int totalManufacturerCount;

    private int totalProductCount;

    private String highestProductName;

    private Double highestProductValue;

    private String lowestProductName;

    private Double lowestProductValue;

    private int noOfManufacturersRecAdded;

    private int noOfProductsRecAdded;

}
