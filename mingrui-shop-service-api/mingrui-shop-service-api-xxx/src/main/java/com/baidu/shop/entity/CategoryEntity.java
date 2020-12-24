package com.baidu.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName CategoryEntity
 * @Description: TODO
 * @Author shenao
 * @Date 2020/12/22
 * @Version V1.0
 **/
@ApiModel(value = "分类实体类")
@Data
@Table(name = "tb_category")
public class CategoryEntity {

    @Id
    @ApiModelProperty(value="分类主键",example = "1")
    private Integer id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "父级分类",example = "1")
    private Integer parentId;

    @ApiModelProperty(value="是否是父级节点",example = "1")
    private Integer isParent;

    @ApiModelProperty(value="排序",example = "1")
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
