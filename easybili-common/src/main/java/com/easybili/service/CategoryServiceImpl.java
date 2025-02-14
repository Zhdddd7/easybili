package com.easybili.service;

import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.CategoryInfo;
import com.easybili.entities.query.CategoryInfoQuery;
import com.easybili.entities.query.VideoInfoQuery;
import com.easybili.mappers.CategoryInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl {
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private VideoInfoServiceImpl videoInfoService;

    @Resource
    private CategoryInfoMapper categoryInfoMapper;

    public int createCategory(CategoryInfo categoryInfo) {
        return categoryInfoMapper.insertCategory(categoryInfo);
    }

    public int updateCategory(CategoryInfo categoryInfo) {
        return categoryInfoMapper.updateCategory(categoryInfo);
    }

    public int deleteCategoryById(Integer categoryId) {
        return categoryInfoMapper.deleteCategoryById(categoryId);
    }

    public int deleteCategoryByCode(String categoryCode){
        return categoryInfoMapper.deleteCategoryByCode(categoryCode);
    }

    public CategoryInfo getCategoryById(Integer categoryId) {
        return categoryInfoMapper.selectCategoryById(categoryId);
    }

    public CategoryInfo getCategoryByCode(String categoryCode){
        return categoryInfoMapper.selectCategoryByCode(categoryCode);
    }

    // consider both linear case & tree case
    public List<CategoryInfo> queryCategories(CategoryInfoQuery query) {
        List<CategoryInfo> categoryInfoList = this.categoryInfoMapper.queryCategories(query);
        if(query.getConvert2Tree() != null & query.getConvert2Tree()){
            categoryInfoList = convertLine2Tree(categoryInfoList, 0);
        }
        return categoryInfoList;
    }


    public void saveCategory(CategoryInfo bean){
        CategoryInfo dbBean = this.categoryInfoMapper.selectCategoryByCode(bean.getCategoryCode());
        if(bean.getCategoryId() == null && dbBean != null ||
                bean.getPCategoryId()!= null && dbBean!= null && !bean.getCategoryId().equals(dbBean.getCategoryId())){
            throw new BusinessException("The category id already exists");
        }
        if (bean.getCategoryId() == null){
            Integer maxSort = this.categoryInfoMapper.selectMaxSort(bean.getPCategoryId());
            bean.setSort(maxSort + 1);
            this.categoryInfoMapper.insertCategory(bean);
        }else{
            this.categoryInfoMapper.updateCategory(bean);
        }
        save2Reids();
    }

    public void delCategories(Integer categoryId){
// TODO here should examine if this category has videos
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setCategoryIdOrPCategoryId(categoryId);
        Integer count = videoInfoService.findCountByQuery(videoInfoQuery);
        if(count > 0){
            throw new BusinessException("Videos detected under this category, please delete them first");
        }

        CategoryInfoQuery categoryInfoQuery = new CategoryInfoQuery();
        categoryInfoQuery.setCategoryIdOrPCategoryId(categoryId);
        this.categoryInfoMapper.deleteCategories(categoryInfoQuery);

        save2Reids();

    }
    // Search the DB in a tree structure
    public List<CategoryInfo> convertLine2Tree(List<CategoryInfo> dataList, Integer pid){
        List<CategoryInfo> children = new ArrayList<>();
        for (CategoryInfo m: dataList){
            if(m.getCategoryId() != null && m.getPCategoryId()!= null && m.getPCategoryId().equals(pid)){
                m.setChildren(convertLine2Tree(dataList, m.getCategoryId()));
                children.add(m);
            }
        }
        return children;
    }
    // change the order of the
    public void changeSort(Integer pCategoryId, String categoryIds){
        String[] categoryIdArray = categoryIds.split(",");
        List<CategoryInfo> categoryInfoList = new ArrayList<>();
        Integer sort = 0;
        // set up a sequence order of the target list
        for (String categoryId: categoryIdArray){
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setCategoryId(Integer.parseInt(categoryId));
            categoryInfo.setPCategoryId(pCategoryId);
            categoryInfo.setSort(++sort);
            categoryInfoList.add(categoryInfo);
        }
        categoryInfoMapper.updateSortBatch(categoryInfoList);

        save2Reids();
    }

    private void save2Reids(){
        CategoryInfoQuery query = new CategoryInfoQuery();
        query.setOrderBy("sort asc");
        query.setConvert2Tree(true);
        List<CategoryInfo> categoryInfoList = queryCategories(query);
        redisComponent.saveCategoryList(categoryInfoList);
    }

    public List<CategoryInfo> getAllCategoryList(){
        List<CategoryInfo> categoryInfoList = redisComponent.getCategoryList();
        if(categoryInfoList.isEmpty()){
            save2Reids();
        }
        return redisComponent.getCategoryList();
    }




}
