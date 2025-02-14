package com.easybili.mappers;

import com.easybili.entities.po.CategoryInfo;
import com.easybili.entities.query.CategoryInfoQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryInfoMapper {
    // Insert a new category
    int insertCategory(CategoryInfo categoryInfo);

    // Update a category
    int updateCategory(CategoryInfo categoryInfo);

    // Delete a category by ID
    int deleteCategoryById(Integer categoryId);

    int deleteCategoryByCode(String categoryCode);

    // Get a category by ID
    CategoryInfo selectCategoryById(Integer categoryId);

    // Get a category by Code
    CategoryInfo selectCategoryByCode(String categoryCode);

    // Query categories with conditions
    List<CategoryInfo> queryCategories(CategoryInfoQuery query);

    // Delete categories with conditions
    int deleteCategories(CategoryInfoQuery query);

    Integer selectMaxSort(Integer pCategoryId);

    void updateSortBatch(List<CategoryInfo> categoryInfoList);
}
