package com.lbi.tile.service;

import com.lbi.tile.dao.DemDao;
import com.lbi.tile.model.ContourPoint;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/*************************************
 * Class Name: DemService
 * Description:〈Dem服务〉
 * @create 2018/5/7
 * @since 1.0.0
 ************************************/
@Service("demService")
public class DemService {
    @Resource(name="demDao")
    DemDao demDao;

    public List<ContourPoint> getHeight_gujiao(List<ContourPoint> ptList)throws Exception{
        //经纬度转像素坐标
        List<ContourPoint> pixelList=new ArrayList<>();
        for(ContourPoint point:ptList){
            GridCoordinates2D pixel=demDao.point2Pixel_gujiao(point.getLongitude(),point.getLatitude());
            if(pixel==null)return null;
            point.setX(pixel.x);
            point.setY(pixel.y);
            pixelList.add(point);
        }

        //线内插值
        List<ContourPoint> list=new ArrayList<>();
        if(pixelList.size()>1){
            //首节点
            ContourPoint point=pixelList.get(0);
            double height=demDao.getHeightByPixel_gujiao(point.getX(),point.getY());
            point.setHeight((int)height);
            list.add(point);

            for(int i=1;i<pixelList.size();i++){
                ContourPoint point1=pixelList.get(i-1);
                ContourPoint point2=pixelList.get(i);
                height=demDao.getHeightByPixel_gujiao(point2.getX(),point2.getY());
                point2.setHeight((int)height);

                //线两端点之间插值
                List<GridCoordinates2D> scanPixellist=demDao.getPixelListByScanLine(point1.getX(),point1.getY(),point2.getX(),point2.getY());
                List<ContourPoint> scanPointList=demDao.fixPoint_gujiao(scanPixellist,point1,point2);
                list.addAll(scanPointList);

                list.add(point2);
            }
        }else {
            ContourPoint point=pixelList.get(0);
            //System.out.println(JSONObject.toJSONString(point));
            double height=demDao.getHeightByPixel_gujiao(point.getX(),point.getY());
            point.setHeight((int)height);
            list.add(point);
        }
        return list;
    }

    public List<ContourPoint> getHeight_jingzhuang(List<ContourPoint> ptList)throws Exception{
        //经纬度转像素坐标
        List<ContourPoint> pixelList=new ArrayList<>();
        for(ContourPoint point:ptList){
            GridCoordinates2D pixel=demDao.point2Pixel_jingzhuang(point.getLongitude(),point.getLatitude());
            if(pixel==null)return null;
            point.setX(pixel.x);
            point.setY(pixel.y);
            pixelList.add(point);
        }

        //线内插值
        List<ContourPoint> list=new ArrayList<>();
        if(pixelList.size()>1){
            //首节点
            ContourPoint point=pixelList.get(0);
            double height=demDao.getHeightByPixel_jingzhuang(point.getX(),point.getY());
            point.setHeight((int)height);
            list.add(point);

            for(int i=1;i<pixelList.size();i++){
                ContourPoint point1=pixelList.get(i-1);
                ContourPoint point2=pixelList.get(i);
                height=demDao.getHeightByPixel_jingzhuang(point2.getX(),point2.getY());
                point2.setHeight((int)height);

                //线两端点之间插值
                List<GridCoordinates2D> scanPixellist=demDao.getPixelListByScanLine(point1.getX(),point1.getY(),point2.getX(),point2.getY());
                List<ContourPoint> scanPointList=demDao.fixPoint_jingzhuang(scanPixellist,point1,point2);
                list.addAll(scanPointList);

                list.add(point2);
            }
        }else {
            ContourPoint point=pixelList.get(0);
            double height=demDao.getHeightByPixel_jingzhuang(point.getX(),point.getY());
            point.setHeight((int)height);
            list.add(point);
        }
        return list;
    }
}
