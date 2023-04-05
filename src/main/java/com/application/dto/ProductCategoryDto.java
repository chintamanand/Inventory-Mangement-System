package com.application.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductCategoryDto implements Serializable {

    private static final long serialVersionUID = -6298641096908510765L;

    private Long productCategoryId;

    private String categoryName;

    private Date lastUpdatedAt;

}
