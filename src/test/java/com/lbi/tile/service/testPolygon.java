/**************************************
 * Copyright (C), Navinfo
 * Package: com.lbi.tile.service
 * Author: liumingkai05559
 * Date: Created in 2018/3/23 17:35
 **************************************/
package com.lbi.tile.service;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.WKTReader;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

/*************************************
 * Class Name: testPolygon
 * Description:〈test〉
 * @author liumingkai
 * @create 2018/3/23
 * @since 1.0.0
 ************************************/
public class testPolygon {
    public final GeometryFactory GEOFACTORY=new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),4326);
    @Test
    public void test(){
        List<LineString> lines=getLines();
        List<Coordinate> ptList=new ArrayList<>();
        for(LineString line:lines){
            Coordinate[] pts=line.getCoordinates();
            for(Coordinate pt:pts){
                ptList.add(pt);
            }
        }
        Coordinate[] points=ptList.toArray(new Coordinate[ptList.size()]);
        Polygon polygon=GEOFACTORY.createPolygon(points);
        System.out.println(polygon.toText());
    }
    private List<LineString> getLines(){
        List<LineString> list=new ArrayList();
        WKTReader reader=new WKTReader();
        try{
            String wkt="LINESTRING (116.401533 39.807303, 116.401499 39.807324)";
            list.add((LineString)reader.read(wkt));
            wkt="LINESTRING (116.401499 39.807324, 116.40148 39.807317, 116.401469 39.807313, 116.401455 39.807304, 116.401447 39.807293, 116.401435 39.807281, 116.40143 39.807258, 116.401422 39.807241, 116.401421 39.807221, 116.401423 39.807208, 116.401428 39.807187, 116.401425 39.807167, 116.401429 39.80715, 116.401443 39.807133, 116.401457 39.807124, 116.401486 39.807109, 116.401518 39.807102, 116.401574 39.807088, 116.40161 39.807088, 116.401675 39.807084, 116.401703 39.807099, 116.401754 39.807118, 116.401768 39.807143, 116.401778 39.807177, 116.401783 39.807202, 116.401783 39.807237, 116.401779 39.807268, 116.401784 39.807287, 116.401769 39.807311, 116.401733 39.807323, 116.401677 39.807328, 116.401647 39.807334, 116.401625 39.807324)";
            list.add((LineString)reader.read(wkt));
            wkt="LINESTRING (116.401625 39.807324, 116.401631 39.807308)";
            list.add((LineString)reader.read(wkt));
            wkt="LINESTRING (116.401631 39.807308, 116.401663 39.807309, 116.40169 39.807305, 116.401717 39.807301, 116.40173 39.807293, 116.401739 39.807282, 116.401741 39.80727, 116.401745 39.807251, 116.401747 39.80723, 116.401741 39.807211, 116.401735 39.807193, 116.401732 39.807165, 116.401723 39.807148, 116.401716 39.807132, 116.40168 39.807123, 116.401662 39.807117, 116.401647 39.807109, 116.401604 39.807111, 116.401568 39.807116, 116.40154 39.807125, 116.401502 39.807135, 116.401479 39.807146, 116.401473 39.807164, 116.401459 39.807183, 116.401459 39.807207, 116.401459 39.807229, 116.401461 39.807247, 116.401466 39.807257, 116.401473 39.807267, 116.40149 39.807283, 116.401512 39.807298, 116.401533 39.807303)";
            list.add((LineString)reader.read(wkt));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
