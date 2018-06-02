/**************************************
 * Copyright (C), Navinfo
 * Package: com.lbi.tile.model
 * Author: liumingkai05559
 * Date: Created in 2018/5/14 22:02
 **************************************/
package com.lbi.tile.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/*************************************
 * Class Name: GeometryVO
 * Description:〈几何对象〉
 * @author liumingkai
 * @create 2018/5/14
 * @since 1.0.0
 ************************************/
@Getter
@Setter
public class GeometryVO {
    @JSONField(ordinal = 1)
    String type;
    @JSONField(ordinal = 2)
    JSONArray coordinates;
}
