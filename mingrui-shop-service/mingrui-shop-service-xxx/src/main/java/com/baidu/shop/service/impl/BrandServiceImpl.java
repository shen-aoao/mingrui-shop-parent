package com.baidu.shop.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author shenao
 * @Date 2020/12/25
 * @Version V1.0
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    private BrandMapper brandMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Autowired
    public void setBrandMapper(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    @Transactional
    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        if(!StringUtils.isEmpty(brandDTO.getSort())) PageHelper.orderBy(brandDTO.getOrderBy());
        if(!StringUtils.isEmpty(brandDTO.getSort())) PageHelper.orderBy(brandDTO.getOrderBy());

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);

        Example example = new Example(BrandEntity.class);
        example.createCriteria().andLike("name","%" + brandEntity.getName() + "%");

        List<BrandEntity> brandEntities = brandMapper.selectByExample(example);
        PageInfo<BrandEntity> pageInfo = new PageInfo<>(brandEntities);
        return this.setResultSuccess(pageInfo);
    }

    @Transactional
    @Override
    public Result<JSONObject> addBrandInfo(BrandDTO brandDTO) {
        //新增返回主键?
        //两种方式实现 select-key insert加两个属性
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.insertSelective(brandEntity);

        //维护中间表
        String categories = brandDTO.getCategories();//得到分类集合字符串
        if(StringUtils.isEmpty(brandDTO.getCategories())) return this.setResultError("");

        //判断分类集合串中是否包含
        if(categories.contains(",")) {
            categoryBrandMapper.insertList(
                    Arrays.asList(categories.split(","))
                    .stream()
                    .map(categoryIdStr -> new CategoryBrandEntity(Integer.valueOf(categoryIdStr)
                            ,brandEntity.getId()))
                    .collect(Collectors.toList())
            );
        }else{
            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(brandEntity.getId());
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));

            categoryBrandMapper.insert(categoryBrandEntity);
            }
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> editBrand(BrandDTO brandDTO) {
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.updateByPrimaryKeySelective(brandEntity);

        //先通过brandId删除中间表的数据
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",brandEntity.getId());

        categoryBrandMapper.deleteByExample(example);

        //批量新增 新增
        String categories = brandDTO.getCategories();
        if(StringUtils.isEmpty(brandDTO.getCategories())) return this.setResultError("");

        //判断分类集合字符串中是否包含,
        if(categories.contains(",")){

            categoryBrandMapper.insertList(
                    Arrays.asList(categories.split(","))
                    .stream()
                    .map(categoryIdStr -> new CategoryBrandEntity(Integer.valueOf(categoryIdStr)
                            ,brandEntity.getId()))
                    .collect(Collectors.toList())
            );
        }else{//普通单个新增
            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(brandEntity.getId());
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));

            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }

        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> deleteBrand(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
        this.deleteCategoryBrandByBrandId(id);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<BrandEntity>> getBrandByCategory(Integer cid) {
        List<BrandEntity> list = brandMapper.getBrandByCategory(cid);

        return this.setResultSuccess(list);
    }

    private void deleteCategoryBrandByBrandId(Integer id){
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",id);
        categoryBrandMapper.deleteByExample(example);
    }


}
