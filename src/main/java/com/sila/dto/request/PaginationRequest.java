package com.sila.dto.request;

import lombok.Data;

@Data
public class PaginationRequest {
    private String filterBy;
    private String search;

    private Integer pageNo = PaginationDefaults.PAGE_NO;
    private Integer pageSize = PaginationDefaults.PAGE_SIZE;
    private String sortBy = PaginationDefaults.SORT_BY;
    private String sortOrder = PaginationDefaults.SORT_ORDER;

    private Double price;         // Add this

    private Double minPrice;  // new field
    private Double maxPrice;  // new field

    private String foodType;      // And this
}