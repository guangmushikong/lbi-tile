/**************************************
 * Copyright (C), Navinfo
 * Package: com.lbi.tile.model
 * Author: liumingkai05559
 * Date: Created in 2018/6/19 10:40
 **************************************/
package com.lbi.tile.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/*************************************
 * Class Name: ContourPoint
 * Description:〈等高线点〉
 * @author liumingkai
 * @create 2018/6/19
 * @since 1.0.0
 ************************************/
@Getter
@Setter
public class ContourPoint {
    /**
     * 经度
     */
    double longitude;
    /**
     * 纬度
     */
    double latitude;
    /**
     * 高度
     */
    int height;
    /**
     * 像素X坐标
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JSONField(serialize=false)
    int x;
    /**
     * 像素Y坐标
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JSONField(serialize=false)
    int y;
    /**
     * 点类型。1为原始点，2为插值点
     */
    int kind;
}
