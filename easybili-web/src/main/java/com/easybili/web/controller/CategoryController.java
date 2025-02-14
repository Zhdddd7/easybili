package com.easybili.web.controller;

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
@RequestMapping("/api/category")
public class CategoryController extends BaseController<String>{

    @Resource
    private CategoryServiceImpl categoryInfoService;

    @RequestMapping("/loadAllCategory")
    public ResponseVO<List<CategoryInfo>> loadAllCategory(CategoryInfoQuery query){
        return ResponseVO.getSuccessResponseVO(categoryInfoService.getAllCategoryList());
    }

}
