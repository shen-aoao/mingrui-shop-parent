package com.baidu.shop.service.impl;

import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.service.CategoryService;
import com.google.gson.JsonObject;
import io.swagger.annotations.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author shenao
 * @Date 2020/12/23
 * @Version V1.0
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result<List<CategoryEntity>> getCategoryByid(Integer pid) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);
        List<CategoryEntity> list = categoryMapper.select(categoryEntity);
        return this.setResultSuccess(list);
    }

    @Override
    public Result<JsonObject> delCategory(Integer id) {
        //验证传入的id是否有效,并且查询出来的数据对接下来的程序有用
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);
        if(categoryEntity == null){
            return this.setResultError("当前id不存在");
        }
        //判断当前节点是否为父节点
        if(categoryEntity.getIsParent() == 1){
            return this.setResultError("当前节点为父节点,不能删除");
        }

        //构建条件查询,通过当前被删除节点的parentid查询数据



        return null;
    }
}
