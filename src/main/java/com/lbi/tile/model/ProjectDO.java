package com.lbi.tile.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProjectDO {
    /**
     * ID，主键
     */
    long id;
    /**
     * 名称
     */
    String name;
    /**
     * 备注
     */
    String memo;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    Date createTime;
    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    Date modifyTime;
}
