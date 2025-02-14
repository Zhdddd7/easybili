package com.easybili.admin.controller;

import com.easybili.entities.po.CategoryInfo;
import com.easybili.entities.query.CategoryInfoQuery;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.CategoryServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController<String>{

    @Resource
    private CategoryServiceImpl categoryInfoService;

    @RequestMapping("/loadCategory")
    public ResponseVO<List<CategoryInfo>> loadCategory(CategoryInfoQuery query){
        query.setOrderBy("sort asc");
        query.setConvert2Tree(true);
        List<CategoryInfo> categoryInfoList = categoryInfoService.queryCategories(query);
        System.out.println(categoryInfoList);
        return ResponseVO.getSuccessResponseVO(categoryInfoList);
    }

    @RequestMapping("/saveCategory")
    public ResponseVO<String> saveCategoru(@NotNull Integer pCategoryId,
                                     Integer categoryId,
                                     @NotEmpty String categoryCode,
                                     @NotEmpty String categoryName,
                                     String icon,
                                     String background){

        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setCategoryId(categoryId);
        categoryInfo.setPCategoryId(pCategoryId);
        categoryInfo.setCategoryCode(categoryCode);
        categoryInfo.setCategoryName(categoryName);
        categoryInfo.setIcon(icon);
        categoryInfo.setBackground(background);
        if(categoryInfo.getSort() == null){
            categoryInfo.setSort(0);
        }

        categoryInfoService.saveCategory(categoryInfo);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/delCategory")
    public ResponseVO<String> deleteCategory(@NotNull Integer categoryId){
        categoryInfoService.delCategories(categoryId);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/changeSort")
    public ResponseVO<String> changeSort(@NotNull Integer pCategoryId, @NotEmpty String categoryIds){
        categoryInfoService.changeSort(pCategoryId, categoryIds);
        return ResponseVO.getSuccessResponseVO();
    }

}
