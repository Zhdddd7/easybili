package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryInfo implements Serializable {
    private Integer categoryId;       // Auto-incrementing category ID
    private String categoryCode;      // Category code
    private String categoryName;      // Category name
    private Integer pCategoryId;      // Parent category ID
    private String icon;              // Icon
    private String background;        // Background image
    private Integer sort;             // Sort order
    private List<CategoryInfo> children; // child object list


}
