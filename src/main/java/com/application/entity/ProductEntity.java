package com.application.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "product")
@Entity
@ToString
public class ProductEntity implements Serializable {

    //product info, origin,expiry date,
    //delivery details, serial number, product box numbers
    //https://material.angular.io/components/table/overview

    private static final long serialVersionUID = -2769127035283753892L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column
    private String productDesc;

    @Column(nullable = false)
    private String productCategory;

    @Column(nullable = false)
    private Long manufacturerId;

    @Column(nullable = false)
    private String manufacturerName;

    @Column
    private Long noOfUnits;

    @Column
    private Double weightOfUnit;

    @Column
    private Double totalWeightOfUnits;

    @Column
    private Double unitCost;

    //These are the costs of shipping, storing, import fees, duties, taxes and
    // other expenses associated with transporting and buying inventory.
    @Column
    private Double landedCost;

    //ActualUnitCost + landedCost = totalCost
    @Column
    private Double totalCost;

    @Column
    private Double totalProductValue;

    //date of received the product
    @Column
    private Date productReceived;

    @Column
    private String productLocation;

    @Column
    private Date lastUpdated;

    @Column
    private String token;

    @Column
    private String privilegeCode;

    @Column
    private boolean accountVerified;

    @Column
    private String password;

    @Column
    private boolean enabled;

    @Column
    private boolean tokenExpired;

}
