package com.chinobot.aiuas.bot_collect.info.util;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "树形对象")
@Data
public class Tree<T> {

	@ApiModelProperty(value = "主键")
	private String id;
	
	@ApiModelProperty(value = "名称")
    private String title;

	@ApiModelProperty(value = "子集合")
    private List<Tree<T>> children;

	@ApiModelProperty(value = "父主键")
    private String parentUUid;
	
	@ApiModelProperty(value = "父类多边形")
	private String lnglats;
    
	@ApiModelProperty(value = "类型：1是领域(不可删除) 2是采查对象(可删除)")
    private String type;

	@ApiModelProperty(value = "是否有父类")
    private boolean hasParent = false;

	@ApiModelProperty(value = "是否有子集")
    private boolean hasChildren = false;

    public void initChildren(){
        this.children = new ArrayList<>();
    }
}
