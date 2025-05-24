package com.sila.util.helper;

import com.sila.dto.request.PaginationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRequest {
    private String search;
    private Boolean seasonal;
    private Boolean vegetarian;
    public static SearchRequest from(PaginationRequest request) {
        return new SearchRequest(
                request.getSearch(),
                request.getSeasonal(),
                request.getVegetarian()
        );
    }
}