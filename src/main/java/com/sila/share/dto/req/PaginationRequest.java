package com.sila.share.dto.req;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sila.share.dto.deserializer.SafeIntegerDeserializer;
import com.sila.share.dto.deserializer.SafeStringDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {
    private String filterBy;
    private String search;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(using = SafeIntegerDeserializer.class)
    private Integer pageNo = PaginationDefaults.PAGE_NO;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(using = SafeIntegerDeserializer.class)
    private Integer pageSize = PaginationDefaults.PAGE_SIZE;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(using = SafeStringDeserializer.class)
    private String sortBy = PaginationDefaults.SORT_BY;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(using = SafeStringDeserializer.class)
    private String sortOrder = PaginationDefaults.SORT_ORDER;


    private Double price;

    private Double minPrice;
    private Double maxPrice;

    private String foodType;
}