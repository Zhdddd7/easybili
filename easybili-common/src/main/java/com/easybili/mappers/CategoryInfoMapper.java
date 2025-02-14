package com.easybili.common.mappers;

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

    // Get a category by ID
    CategoryInfo selectCategoryById(Integer categoryId);

    // Query categories with conditions
    List<CategoryInfo> queryCategories(CategoryInfoQuery query);
}
