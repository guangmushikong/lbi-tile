/**************************************
 * Copyright (C), Navinfo
 * Package: com.lbi.tile.service
 * Author: liumingkai05559
 * Date: Created in 2018/5/7 10:55
 **************************************/
package com.lbi.tile.service;

import com.alibaba.fastjson.JSONObject;
import com.lbi.tile.dao.DemDao;
import com.vividsolutions.jts.geom.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/*************************************
 * Class Name: DemService
 * Description:〈Dem服务〉
 * @author liumingkai
 * @create 2018/5/7
 * @since 1.0.0
 ************************************/
@Service("demService")
public class DemService {
    @Resource(name="demDao")
    DemDao demDao;
    final GeometryFactory GEO_FACTORY=new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),4326);

    public List<JSONObject> getHeight(Coordinate[] coords){
        List<JSONObject> list=new ArrayList<>();
        List<Point> pointList;
        if(coords.length==1){
            Point point=GEO_FACTORY.createPoint(coords[0]);
            pointList=demDao.getHeightByPoint(point);
        }else if(coords.length>1){
            LineString line=GEO_FACTORY.createLineString(coords);
            pointList=demDao.getHeightByLineString(line);
        }else{
            pointList=new ArrayList<>();
        }
        for(Point point:pointList){
            JSONObject u=new JSONObject();
            u.put("x",point.getX());
            u.put("y",point.getY());
            u.put("height",point.getUserData());
            list.add(u);
        }
        return list;
    }
}
