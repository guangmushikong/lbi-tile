package com.lbi.tile.dao;

import com.alibaba.fastjson.JSONObject;


import com.lbi.model.Tile;
import com.lbi.util.TileSystem;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.geojson.geom.GeometryJSON;
import org.junit.Before;
import org.junit.Test;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testDao {
    JdbcTemplate jdbcTemplate;
    final GeometryFactory GEO_FACTORY=new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),4326);
    @Before
    public void init(){
        /*try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://localhost:5433/postgres");
            dataSource.setUsername("postgres");
            dataSource.setPassword("postgres");
            dataSource.setMinIdle(10);
            dataSource.setMaxIdle(100);
            dataSource.setInitialSize(10);
            dataSource.setMaxActive(100);
            jdbcTemplate=new JdbcTemplate();
            jdbcTemplate.setDataSource(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    @Test
    public void getTotal(){
        String sql="select count(*) as total from data.gujiao_50_16";
        long total= jdbcTemplate.queryForObject(sql,Integer.class);
        System.out.println("total:"+total);
    }

    @Test
    public void getTile()throws Exception{
        Tile tile=new Tile(1659,789,11);
        List<JSONObject> contourList=getContourList(tile);
        WKTReader reader=new WKTReader();
        for(int i=0;i<contourList.size();i++){
            JSONObject contour=contourList.get(i);
            //String wkt=contour.getWkt();
            //LineString line=(LineString)reader.read(wkt);
            System.out.println(contour.toJSONString());
        }
        //JSONObject obj=JSONObject.parseObject(geojson);
        //System.out.println(obj.toJSONString());
    }
    @Test
    public void clipEnve()throws Exception{
        Tile tile=new Tile(1659,789,11);
        WKTReader reader=new WKTReader();
        String wkt="LINESTRING(111.72165543018 38.0024736235105,111.721480906128 38.0025858718042,111.72165543018 38.0024736235105)";
        Geometry line=reader.read(wkt);
        System.out.println(line.toText());
        PrecisionModel pm=new PrecisionModel(PrecisionModel.FLOATING_SINGLE);
        GeometryFactory geometryFactory=new GeometryFactory(pm,4326);
        Geometry geom=geometryFactory.createGeometry(line);
        System.out.println(geom.toText());
        String json = null;
        StringWriter writer = new StringWriter();
        GeometryJSON g = new GeometryJSON();
        Map<String,Object> userData=new HashMap<>();
        userData.put("id",1313);
        userData.put("contour",1500);
        line.setUserData(userData);
        g.write(line,writer);
        json = writer.toString();
        System.out.println(json);

        /*Coordinate[] pts=line.getCoordinates();
        for(int i=0;i<pts.length;i++){
            Coordinate pt=pts[i];
            Point point=geometryFactory.createPoint(pt);

            System.out.println(i+"|"+pt.toString()+"|"+point.toText());
        }*/

    }
    private Coordinate NumberScale(Coordinate pt, int size){
        BigDecimal xDecimal = new BigDecimal(pt.x);
        BigDecimal yDecimal = new BigDecimal(pt.y);
        double x=xDecimal.setScale(size,BigDecimal.ROUND_HALF_DOWN).doubleValue();
        double y=yDecimal.setScale(size,BigDecimal.ROUND_HALF_DOWN).doubleValue();
        return new Coordinate(x,y);
    }

    private List<JSONObject> getContourList(Tile tile){
        List<JSONObject> list=null;
        try{
            Envelope enve= TileSystem.TileXYToBounds(tile);
            System.out.println(enve.toString());
            StringBuilder sb=new StringBuilder();
            String enveSql="ST_MakeEnvelope("+enve.getMinX()+","+enve.getMinY()+","+enve.getMaxX()+","+enve.getMaxY()+",4326)";

            sb.append("select json_build_object('features',array_to_json(array(");
            sb.append("select json_build_object('geometry',ST_AsGeoJSON(geom)::json,'properties',json_build_object('id',id,'contour',contour),'type','Feature') as geometry");
            sb.append(" from (");
            sb.append("select id,contour");
            sb.append(",st_clipbybox2d(geom,"+enveSql+") as geom");
            sb.append(" from data.gujiao_100_11_new");
            sb.append(" where st_intersects("+enveSql+",geom)");
            sb.append(") t))");
            sb.append(") as features");

            list=jdbcTemplate.query(
                    sb.toString(),
                    new RowMapper<JSONObject>() {
                        public JSONObject mapRow(ResultSet rs, int i) throws SQLException {
                            String geojson=rs.getString("features");
                            return JSONObject.parseObject(geojson);
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

}
