package com.lbi.tile.dao;

import com.lbi.tile.model.*;

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

@Repository(value="tmsDao")
public class TMSDao {
    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<T_TileMap> getTileMapList(){
        List<T_TileMap> list=null;
        try{
            String sql="select * from t_tile_map order by id";
            list=jdbcTemplate.query(
                    sql,
                    new RowMapper<T_TileMap>() {
                        public T_TileMap mapRow(ResultSet rs, int i) throws SQLException {
                            T_TileMap u=new T_TileMap();
                            u.setId(rs.getInt("id"));
                            u.setLayerName(rs.getString("layer_name"));
                            u.setTitle(rs.getString("title"));
                            u.setProfile(rs.getString("profile"));
                            u.setSrs(rs.getString("srs"));
                            u.setHref(rs.getString("href"));
                            u.setUrl(rs.getString("url"));
                            u.setMinX(rs.getDouble("minx"));
                            u.setMinY(rs.getDouble("miny"));
                            u.setMaxX(rs.getDouble("maxx"));
                            u.setMaxY(rs.getDouble("maxy"));
                            u.setOriginX(rs.getDouble("origin_x"));
                            u.setOriginY(rs.getDouble("origin_y"));
                            u.setTileWidth(rs.getInt("tile_width"));
                            u.setTileHeight(rs.getInt("tile_height"));
                            u.setMimeType(rs.getString("mime_type"));
                            u.setExtension(rs.getString("format_extension"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
    public T_TileMap getTileMapById(String title,String srs,String formatExtension){
        try{
            String sql="select * from t_tile_map where title=? and srs=? and format_extension=?";
            List<T_TileMap> list=jdbcTemplate.query(
                    sql,
                    new Object[]{
                            title,
                            srs,
                            formatExtension
                    },
                    new int[]{
                            Types.VARCHAR,
                            Types.VARCHAR,
                            Types.VARCHAR,
                    },
                    new RowMapper<T_TileMap>() {
                        public T_TileMap mapRow(ResultSet rs, int i) throws SQLException {
                            T_TileMap u=new T_TileMap();
                            u.setId(rs.getInt("id"));
                            u.setLayerName(rs.getString("layer_name"));
                            u.setTitle(rs.getString("title"));
                            u.setProfile(rs.getString("profile"));
                            u.setSrs(rs.getString("srs"));
                            u.setHref(rs.getString("href"));
                            u.setUrl(rs.getString("url"));
                            u.setSType(rs.getInt("s_type"));
                            u.setMinX(rs.getDouble("minx"));
                            u.setMinY(rs.getDouble("miny"));
                            u.setMaxX(rs.getDouble("maxx"));
                            u.setMaxY(rs.getDouble("maxy"));
                            u.setOriginX(rs.getDouble("origin_x"));
                            u.setOriginY(rs.getDouble("origin_y"));
                            u.setTileWidth(rs.getInt("tile_width"));
                            u.setTileHeight(rs.getInt("tile_height"));
                            u.setMimeType(rs.getString("mime_type"));
                            u.setExtension(rs.getString("format_extension"));
                            return u;
                        }
                    });
            if(list.size()>0)return list.get(0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public List<T_TileSet> getTileSetListByID(int mapId){
        List<T_TileSet> list=null;
        try{
            String sql="select * from t_tile_set where map_id=? order by set_order";
            list=jdbcTemplate.query(
                    sql,
                    new Object[]{mapId},
                    new int[]{Types.INTEGER},
                    new RowMapper<T_TileSet>() {
                        public T_TileSet mapRow(ResultSet rs, int i) throws SQLException {
                            T_TileSet u=new T_TileSet();
                            u.setHref(rs.getString("href"));
                            u.setUnits_per_pixel(rs.getString("units_per_pixel"));
                            u.setOrder(rs.getString("set_order"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
}
