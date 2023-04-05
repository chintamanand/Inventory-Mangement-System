package com.application.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "stock_purchase")
@Entity
@ToString
public class StockPurchaseEntity implements Serializable {

    //product info, no of units,cost of each unit,product belongs to which manufactutrer, origin,expiry date,
    //date of received the product,delivery details, serial number, product box numbers
    //https://material.angular.io/components/table/overview

    private static final long serialVersionUID = 2222593343104168066L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    //this has to be valid manufacturerId
    @Column(nullable = false)
    private Long manufacturerId;

    @Column(nullable = false)
    private String manufacturerName;

    //this has to be valid productId
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productCategoryName;

    @Column(nullable = false)
    private long noOfUnits;

    @Column(nullable = false)
    private double weightOfUnit;

    //TotalWeight = noOfUnits * weightOfUnit
    @Column
    private double totalWeight;

    //Amount Paid and productValue has to be same
    @Column
    private double productValue;

    @Column
    private double amountPaid;

    @Column
    private String paymentMethod;

    @Column
    private String payeeName;

    @Column
    private String paymentMetaInfo;

    @Column
    private String phoneNumber;

    @Column
    private String emailAddress;

    //after payment success, add product with correct info into product table;

}
