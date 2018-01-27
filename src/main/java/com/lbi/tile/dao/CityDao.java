package com.lbi.tile.dao;

import com.lbi.map.Tile;
import com.lbi.map.TileSystem;
import com.lbi.tile.model.Admin_Region;
import com.lbi.util.GeoUtils;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository(value="cityDao")
public class CityDao {
    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,String>> getCityList(){
        List<Map<String,String>> list=null;
        try{
            String sql="select adcode,name,name_alias,x,y,minx,miny,maxx,maxy from s_ods_city_simplify order by adcode";
            list=jdbcTemplate.query(
                    sql,
                    new RowMapper<Map<String,String>>() {
                        public Map<String,String> mapRow(ResultSet rs, int i) throws SQLException {
                            Map<String,String> u=new HashMap();
                            u.put("adcode",rs.getString("adcode"));
                            u.put("name",rs.getString("name"));
                            u.put("x",rs.getString("x"));
                            u.put("y",rs.getString("y"));
                            u.put("minx",rs.getString("minx"));
                            u.put("miny",rs.getString("miny"));
                            u.put("maxx",rs.getString("maxx"));
                            u.put("maxy",rs.getString("maxy"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
    public List<Admin_Region> getCityRegionList(Tile tile){
        List<Admin_Region> list=null;
        try{
            Envelope enve= TileSystem.TileXYToBounds(tile);
            Geometry grid= GeoUtils.GEO_FACTORY.toGeometry(enve);
            StringBuilder sb=new StringBuilder();
            String[] fields={"adcode","name","st_astext(geom) as wkt"};
            sb.append("select ").append(StringUtils.join(fields,',')).append(" from s_ods_city_simplify");
            sb.append(" where st_intersects(st_geomfromtext(?,4326),geom)");
            list=jdbcTemplate.query(
                    sb.toString(),
                    new Object[]{grid.toText()},
                    new int[]{Types.VARCHAR},
                    new RowMapper<Admin_Region>() {
                        public Admin_Region mapRow(ResultSet rs, int i) throws SQLException {
                            Admin_Region u=new Admin_Region();
                            u.setCode(rs.getString("adcode"));
                            u.setName(rs.getString("name"));
                            u.setWkt(rs.getString("wkt"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
}
