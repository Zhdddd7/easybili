package com.easybili.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class PaginationResultVO<T> {
    private Integer totalCount;
    private Integer pageSize;
    private Integer pageNo;
    private Integer pageTotal;
    private List<T> list = new ArrayList<>();

    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageNo, List<T> list){
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.list = list;
    }

    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageNo, Integer pageTotal, List<T> list){
        if(pageNo ==0){
            pageNo = 1;
        }
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.pageTotal = pageTotal;
        this.list = list;
    }


}





