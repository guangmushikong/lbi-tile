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

@Repository(value="tileDao")
public class TileDao {
    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

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

    public List<Map<String,String>> getGeometryByTile(String tableName,Tile tile){
        List<Map<String,String>> list=null;
        try{
            Envelope enve= TileSystem.TileXYToBounds(tile);
            Geometry grid= GeoUtils.GEO_FACTORY.toGeometry(enve);
            StringBuilder sb=new StringBuilder();
            String[] fields={"id","contour","st_astext(ST_Transform(geom,4326)) as wkt"};
            sb.append("select ").append(StringUtils.join(fields,',')).append(" from "+tableName);
            sb.append(" where st_intersects(st_geomfromtext('"+grid.toText()+"',4326),ST_Transform(geom,4326))");
            //System.out.println(sb.toString());
            list=jdbcTemplate.query(
                    sb.toString(),
                    new RowMapper<Map<String,String>>() {
                        public Map<String,String> mapRow(ResultSet rs, int i) throws SQLException {
                            Map<String,String> u=new HashMap<>();
                            u.put("id",rs.getString("id"));
                            u.put("contour",rs.getString("contour"));
                            u.put("wkt",rs.getString("wkt"));
                            return u;
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
