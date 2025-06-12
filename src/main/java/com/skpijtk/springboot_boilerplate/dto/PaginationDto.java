package com.skpijtk.springboot_boilerplate.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto<T> {

    private List<T> data;
    private long totalData;
    private int totalPage;
    private int currentPage;
    private int pageSize;

}
