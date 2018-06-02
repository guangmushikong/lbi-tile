/**************************************
 * Copyright (C), Navinfo
 * Package: com.lbi.tile.controller
 * Author: liumingkai05559
 * Date: Created in 2018/5/7 10:58
 **************************************/
package com.lbi.tile.controller;

import com.alibaba.fastjson.JSONObject;
import com.lbi.tile.model.ResultBody;
import com.lbi.tile.service.DemService;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*************************************
 * Class Name: DemController
 * Description:〈Dem控制器〉
 * @author liumingkai
 * @create 2018/5/7
 * @since 1.0.0
 ************************************/
@RestController
@RequestMapping("/dem")
public class DemController {
    @Resource(name="demService")
    private DemService demService;

    @RequestMapping(value="/contour",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody contour(@RequestParam("xys") String xys) {
        if(StringUtils.isNoneEmpty(xys)){
            String[] arr=xys.split(";");
            Coordinate[] coords=new Coordinate[arr.length];
            for(int i=0;i<arr.length;i++){
                String tmp=arr[i];
                String[] pts=tmp.split(",");
                double x=Double.parseDouble(pts[0]);
                double y=Double.parseDouble(pts[1]);
                Coordinate pt=new Coordinate(x,y);
                coords[i]=pt;
            }
            List<JSONObject> list=demService.getHeight(coords);
            return new ResultBody<>(list);
        }else {
            return new ResultBody<>(-1,"xys不能为空");
        }
    }
}
