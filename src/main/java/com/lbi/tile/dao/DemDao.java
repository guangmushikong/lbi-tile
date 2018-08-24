package com.lbi.tile.dao;

import com.lbi.tile.model.ContourPoint;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*************************************
 * Class Name: DemDao
 * Description:〈DEM数据Dao〉
 * @create 2018/5/7
 * @since 1.0.0
 ************************************/
@Repository(value="demDao")
public class DemDao {
    @Resource(name="coverage_gujiao")
    GridCoverage2D coverage_gujiao;
    @Resource(name="coverage_jingzhuang")
    GridCoverage2D coverage_jingzhuang;

    Raster grid_gujiao;
    Raster grid_jingzhuang;

    /**
     * 经纬度坐标转像素坐标
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     * @throws Exception
     */
    public GridCoordinates2D point2Pixel_gujiao(double longitude,double latitude)throws Exception {
        DirectPosition2D position = new DirectPosition2D();
        position.setLocation(longitude,latitude);
        GridCoordinates2D posGrid = coverage_gujiao.getGridGeometry().worldToGrid(position);
        return posGrid;
    }

    public GridCoordinates2D point2Pixel_jingzhuang(double longitude,double latitude)throws Exception {
        DirectPosition2D position = new DirectPosition2D();
        position.setLocation(longitude,latitude);
        GridCoordinates2D posGrid = coverage_jingzhuang.getGridGeometry().worldToGrid(position);
        return posGrid;
    }

    /**
     * 获取像素坐标高度
     * @param pixelX    像素坐标X值
     * @param pixelY    像素坐标Y值
     * @return 高度
     */
    public double getHeightByPixel_gujiao(int pixelX,int pixelY){
        getGridData_gujiao();
        double[] data = grid_gujiao.getPixel(pixelX, pixelY, new double[1]);
        return data[0];
    }

    public double getHeightByPixel_jingzhuang(int pixelX,int pixelY)throws Exception{
        getGridData_jingzhuang();
        double[] data = grid_jingzhuang.getPixel(pixelX, pixelY, new double[1]);
        return data[0];
    }

    /**
     * 清理插值点坐标数据
     * @param posGridList
     * @return
     * @throws Exception
     */
    public List<ContourPoint> fixPoint_gujiao(List<GridCoordinates2D> posGridList,ContourPoint fPoint,ContourPoint tPoint)throws Exception{
        double height;
        Queue<ContourPoint> queue=new LinkedList<>();
        for(GridCoordinates2D posGrid:posGridList){
            DirectPosition pt=pixel2Point_gujiao(posGrid.x,posGrid.y);
            double[] xys=pt.getCoordinate();
            height=getHeightByPixel_gujiao(posGrid.x,posGrid.y);
            ContourPoint point=new ContourPoint();
            point.setLongitude(xys[0]);
            point.setLatitude(xys[1]);
            point.setX(posGrid.x);
            point.setY(posGrid.y);
            point.setKind(2);
            point.setHeight((int)height);
            queue.add(point);
        }

        //寻找单调区间
        ContourPoint a=queue.poll();
        ContourPoint b;
        char fAttr,tAttr,attr='0';
        if(a.getHeight() > fPoint.getHeight())fAttr='+';
        else if(a.getHeight() == fPoint.getHeight())fAttr='=';
        else fAttr='-';
        //System.out.println(a.getHeight()+"|fAttr:"+fAttr);

        List<ContourPoint> list=new ArrayList<>();
        while((b=queue.peek())!=null){
            if(b.getHeight() > a.getHeight())attr='+';
            else if(b.getHeight() == a.getHeight())attr='=';
            else attr='-';
            //System.out.println(b.getHeight()+"|attr:"+attr);

            if(fAttr!=attr){
                list.add(a);
                fAttr=attr;
            }
            a=queue.poll();
        }
        //比较尾节点
        if(tPoint.getHeight() > a.getHeight())tAttr='+';
        else if(tPoint.getHeight() == a.getHeight())tAttr='=';
        else tAttr='-';
        //System.out.println(tPoint.getHeight()+"|tAttr:"+tAttr);
        if(attr!='0' && tAttr!=attr)list.add(a);

        return list;
    }

    public List<ContourPoint> fixPoint_jingzhuang(List<GridCoordinates2D> posGridList,ContourPoint fPoint,ContourPoint tPoint)throws Exception{
        double height;
        Queue<ContourPoint> queue=new LinkedList<>();
        for(GridCoordinates2D posGrid:posGridList){
            DirectPosition pt=pixel2Point_jingzhuang(posGrid.x,posGrid.y);
            double[] xys=pt.getCoordinate();
            height=getHeightByPixel_jingzhuang(posGrid.x,posGrid.y);
            ContourPoint point=new ContourPoint();
            point.setLongitude(xys[0]);
            point.setLatitude(xys[1]);
            point.setX(posGrid.x);
            point.setY(posGrid.y);
            point.setKind(2);
            point.setHeight((int)height);
            queue.add(point);
        }

        //寻找单调区间
        ContourPoint a=queue.poll();
        ContourPoint b;
        char fAttr,tAttr,attr='0';
        if(a.getHeight() > fPoint.getHeight())fAttr='+';
        else if(a.getHeight() == fPoint.getHeight())fAttr='=';
        else fAttr='-';
        //System.out.println(a.getHeight()+"|fAttr:"+fAttr);

        List<ContourPoint> list=new ArrayList<>();
        while((b=queue.peek())!=null){
            if(b.getHeight() > a.getHeight())attr='+';
            else if(b.getHeight() == a.getHeight())attr='=';
            else attr='-';
            //System.out.println(b.getHeight()+"|attr:"+attr);

            if(fAttr!=attr){
                list.add(a);
                fAttr=attr;
            }
            a=queue.poll();
        }
        //比较尾节点
        if(tPoint.getHeight() > a.getHeight())tAttr='+';
        else if(tPoint.getHeight() == a.getHeight())tAttr='=';
        else tAttr='-';
        //System.out.println(tPoint.getHeight()+"|tAttr:"+tAttr);
        if(attr!='0' && tAttr!=attr)list.add(a);

        return list;
    }


    /**
     * 获取线两个端点之间像素点
     * @param px1
     * @param py1
     * @param px2
     * @param py2
     * @return
     */
    public List<GridCoordinates2D> getPixelListByScanLine(int px1,int py1,int px2,int py2){
        List<GridCoordinates2D> pixelList=new ArrayList<>();
        //System.out.println("pt1:"+px1+","+py1+"|pt2:"+px2+","+py2);
        if(px1==px2){
            if(py2<py1){
                for(int py=py1-1;py>py2;py--){
                    GridCoordinates2D pixel=new GridCoordinates2D(px1,py);
                    pixelList.add(pixel);
                }
            }else {
                for(int py=py1+1;py<py2;py++){
                    GridCoordinates2D pixel=new GridCoordinates2D(px1,py);
                    pixelList.add(pixel);
                }
            }
        }else if(py1 == py2){
            if(px2<px1){
                for(int px=px1-1;px>px2;px--){
                    GridCoordinates2D pixel=new GridCoordinates2D(px,py1);
                    pixelList.add(pixel);
                }
            }else {
                for(int px=px1+1;px<px2;px++){
                    GridCoordinates2D pixel=new GridCoordinates2D(px,py1);
                    pixelList.add(pixel);
                }
            }
        }else{
            double k=(py2-py1)*1.0/(px2-px1);
            int px=px1;int py=py1;
            int xOffset=1;int yOffset=1;
            if(px2<px1)xOffset=-1;
            if(py2<py1)yOffset=-1;

            if(Math.abs(py2-py1)>Math.abs(px2-px1)){
                while((py+yOffset)!=py2){
                    py=py+yOffset;
                    px=(int)Math.round(px1+(py-py1)/k);
                    GridCoordinates2D pixel=new GridCoordinates2D(px,py);
                    pixelList.add(pixel);
                }
            }else {
                while((px+xOffset)!=px2){
                    px=px+xOffset;
                    py=(int)Math.round(py1+(px-px1)*k);
                    GridCoordinates2D pixel=new GridCoordinates2D(px,py);
                    pixelList.add(pixel);
                }
            }
        }
        return pixelList;
    }

    /**
     * 像素点坐标转经纬度
     * @param pixelX
     * @param pixelY
     * @return
     * @throws Exception
     */
    private DirectPosition pixel2Point_gujiao(int pixelX,int pixelY)throws Exception {
        GridCoordinates2D posGrid=new GridCoordinates2D(pixelX,pixelY);
        DirectPosition position=coverage_gujiao.getGridGeometry().gridToWorld(posGrid);
        return position;
    }

    private DirectPosition pixel2Point_jingzhuang(int pixelX,int pixelY)throws Exception {
        GridCoordinates2D posGrid=new GridCoordinates2D(pixelX,pixelY);
        DirectPosition position=coverage_jingzhuang.getGridGeometry().gridToWorld(posGrid);
        return position;
    }

    /**
     * 获取DEM栅格
     * @return
     */
    private void getGridData_gujiao(){
        if(grid_gujiao==null){
            grid_gujiao=coverage_gujiao.getRenderedImage().getData();
            System.out.println("init grid_gujiao");
        }
    }
    /**
     * 获取DEM栅格
     * @return
     */
    private void getGridData_jingzhuang(){
        if(grid_jingzhuang==null){
            grid_jingzhuang=coverage_jingzhuang.getRenderedImage().getData();
            System.out.println("init grid_jingzhuang");
        }
    }
}
