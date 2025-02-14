package com.easybili.entities.query;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CategoryInfoQuery {
    private String categoryCode;  // Category code (filter)
    private String categoryName;  // Category name (filter)

    // Pagination attributes
    private Integer pageNum = 1;      // Current page
    private Integer pageSize = 10;     // Page size

    private Boolean convert2Tree; // to do a recursive query

    private String orderBy; // order condition

    private Integer categoryIdOrPCategoryId; // to examine an id exists in a tree structure

    public Integer getOffset() {
        if (pageNum != null && pageSize != null) {
            return (pageNum - 1) * pageSize;
        }
        return null;
    }


}



