package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "品牌接口")
public interface BrandService {

    @ApiOperation(value = "获取品牌信息")
    @GetMapping(value = "brand/getBrandInfo")
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO);

    @ApiOperation(value="新增品牌")
    @PostMapping(value="brand/addBrandInfo")
    public Result<JSONObject> addBrandInfo(@Validated({MingruiOperation.Add.class})@RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "修改品牌信息")
    @PutMapping(value = "brand/addBrandInfo")
    Result<JSONObject> editBrand(@Validated({MingruiOperation.Update.class}) @RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "通过id删除品牌信息")
    @DeleteMapping(value="brand/delete")
    Result<JSONObject> deleteBrand(Integer id);

    @ApiOperation(value = "通过分类id获取品牌")
    @GetMapping(value="brand/getBrandByCategory")
    public Result<List<BrandEntity>> getBrandByCategory(Integer cid);
}
