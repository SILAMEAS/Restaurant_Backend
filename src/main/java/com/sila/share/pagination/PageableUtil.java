package com.sila.share.pagination;

import com.sila.share.dto.req.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class PageableUtil {

    public static Pageable fromRequest(PaginationRequest request) {
        int page = Math.max(0, request.getPageNo() - 1); // avoid negative page index

        Sort.Direction direction = Sort.Direction.fromString(request.getSortOrder().toUpperCase());

        return PageRequest.of(
                page,
                request.getPageSize(),
                Sort.by(direction, request.getSortBy())
        );
    }
}