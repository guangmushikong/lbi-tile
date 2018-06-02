/**************************************
 * Copyright (C), Navinfo
 * Package: com.lbi.tile.dao
 * Author: liumingkai05559
 * Date: Created in 2018/5/7 10:48
 **************************************/
package com.lbi.tile.dao;

import com.vividsolutions.jts.geom.*;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;

/*************************************
 * Class Name: DemDao
 * Description:〈DEM数据Dao〉
 * @create 2018/5/7
 * @since 1.0.0
 ************************************/
@Repository(value="demDao")
public class DemDao {
    @Resource(name="coverage")
    GridCoverage2D coverage;
    final GeometryFactory GEO_FACTORY=new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),4326);
    Raster gridData;

    public List<Point> getHeightByPoint(Point point){
        List<Point> pointList=new ArrayList<>();
        Raster gridData=getGridDate();
        DirectPosition2D position = new DirectPosition2D();
        position.setLocation(point.getX(), point.getY());
        try{
            GridCoordinates2D posGrid = coverage.getGridGeometry().worldToGrid(position);
            double[] data = gridData.getPixel(posGrid.x, posGrid.y, new double[1]);
            double height=data[0];
            point.setUserData(height);
            pointList.add(point);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pointList;
    }
    public List<Point> getHeightByLineString(LineString line){
        List<Point> pointList=new ArrayList<>();
        Raster gridData=getGridDate();
        try{
            Coordinate[] coords=line.getCoordinates();
            for(Coordinate pt:coords){
                DirectPosition2D position = new DirectPosition2D();
                position.setLocation(pt.x, pt.y);
                GridCoordinates2D posGrid = coverage.getGridGeometry().worldToGrid(position);
                double[] data = gridData.getPixel(posGrid.x, posGrid.y, new double[1]);
                double height=data[0];
                Point point=GEO_FACTORY.createPoint(pt);
                point.setUserData(height);
                pointList.add(point);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pointList;
    }
    private List<Point> getScanPoint(Coordinate pt1,Coordinate pt2){
        List<Point> pointList=new ArrayList<>();
        DirectPosition2D position1 = new DirectPosition2D();
        position1.setLocation(pt1.x, pt1.y);
        DirectPosition2D position2 = new DirectPosition2D();
        position1.setLocation(pt2.x, pt2.y);
        try{
            GridCoordinates2D posGrid1 = coverage.getGridGeometry().worldToGrid(position1);
            GridCoordinates2D posGrid2 = coverage.getGridGeometry().worldToGrid(position2);


            int pixelX1=posGrid1.x;
            int pixelX2=posGrid2.x;
            int pixelY1=posGrid1.y;
            int pixelY2=posGrid2.y;
            int pixelX=pixelX1;
            int pixelY=pixelY1;
            while(pixelX!=pixelX2){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pointList;
    }
    private Raster getGridDate(){
        if(gridData==null){
            gridData=coverage.getRenderedImage().getData();
            System.out.println("init gridData");
        }
        return gridData;
    }
}
