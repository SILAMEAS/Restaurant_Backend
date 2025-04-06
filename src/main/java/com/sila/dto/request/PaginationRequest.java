package com.sila.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {
    Integer pageNo;
    Integer pageSize;
    String sortBy;
    Sort.Direction sortOrder;
}
