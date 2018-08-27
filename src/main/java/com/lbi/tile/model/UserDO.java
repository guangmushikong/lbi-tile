package com.lbi.tile.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/*************************************
 * Class Name: UserDO
 * Description:〈用户〉
 * @create 2018/8/26
 * @since 1.0.0
 ************************************/
@Getter
@Setter
public class UserDO {
    /**
     * ID，主键
     */
    long id;
    /**
     * 名称
     */
    String name;
    /**
     * 用户等级
     */
    int level;
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
