/**************************************
 * Copyright (C), Navinfo
 * Package: com.lbi.tile.model
 * Author: liumingkai05559
 * Date: Created in 2018/5/14 22:01
 **************************************/
package com.lbi.tile.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/*************************************
 * Class Name: FeatureVO
 * Description:〈要素对象〉
 * @author liumingkai
 * @create 2018/5/14
 * @since 1.0.0
 ************************************/
@Getter
@Setter
public class FeatureVO {
    @JSONField(ordinal = 1)
    String type;
    @JSONField(ordinal = 2)
    GeometryVO geometry;
    @JSONField(ordinal = 3)
    JSONObject properties;
}
