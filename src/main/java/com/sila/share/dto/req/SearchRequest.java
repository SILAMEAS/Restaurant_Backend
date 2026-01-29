package com.sila.share.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String search;

    public static SearchRequest from(PaginationRequest request) {
        return new SearchRequest(
                request.getSearch()
        );
    }

}
