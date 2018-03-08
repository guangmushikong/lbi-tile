package com.lbi.tile.dao;

import com.lbi.tile.model.TileMap;
import com.lbi.tile.model.TileSet;
import com.lbi.tile.model.TileMapService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository(value="metaDao")
public class MetaDao {
    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<TileMapService> getTileMapServiceList(){
        List<TileMapService> list=null;
        try{
            String sql="select * from t_tilemapservice order by id";
            list=jdbcTemplate.query(
                    sql,
                    new RowMapper<TileMapService>() {
                        public TileMapService mapRow(ResultSet rs, int i) throws SQLException {
                            TileMapService u=new TileMapService();
                            u.setId(rs.getLong("id"));
                            u.setTitle(rs.getString("title"));
                            u.setAbstract(rs.getString("abstract"));
                            u.setVersion(rs.getString("version"));
                            u.setHref(rs.getString("href"));
                            u.setKind(rs.getInt("kind"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public TileMapService getTileMapServiceById(long serviceId){
        try{
            String sql="select * from t_tilemapservice where id=?";
            List<TileMapService>  list=jdbcTemplate.query(
                    sql,
                    new Object[]{serviceId},
                    new int[]{Types.BIGINT},
                    new RowMapper<TileMapService>() {
                        public TileMapService mapRow(ResultSet rs, int i) throws SQLException {
                            TileMapService u=new TileMapService();
                            u.setId(rs.getLong("id"));
                            u.setTitle(rs.getString("title"));
                            u.setAbstract(rs.getString("abstract"));
                            u.setVersion(rs.getString("version"));
                            u.setHref(rs.getString("href"));
                            u.setKind(rs.getInt("kind"));
                            return u;
                        }
                    });
            if(list.size()>0)return list.get(0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<TileMap> getTileMapList(long serviceId){
        List<TileMap> list=null;
        try{
            StringBuilder sb=new StringBuilder();
            sb.append("select t1.*,t2.min_zoom,t2.max_zoom from t_tilemap t1 join");
            sb.append(" (select map_id,min(sort_order) as min_zoom,max(sort_order) as max_zoom from t_tileset group by map_id) t2");
            sb.append(" on t1.id=t2.map_id");
            sb.append(" where t1.service_id=?");
            sb.append(" order by t1.title");
            list=jdbcTemplate.query(
                    sb.toString(),
                    new Object[]{serviceId},
                    new int[]{Types.BIGINT},
                    new RowMapper<TileMap>() {
                        public TileMap mapRow(ResultSet rs, int i) throws SQLException {
                            TileMap u=new TileMap();
                            u.setId(rs.getLong("id"));
                            u.setServiceId(rs.getLong("service_id"));
                            u.setTitle(rs.getString("title"));
                            u.setAbstract(rs.getString("abstract"));
                            u.setSrs(rs.getString("srs"));
                            u.setProfile(rs.getString("profile"));
                            u.setHref(rs.getString("href"));

                            u.setMinX(rs.getDouble("minx"));
                            u.setMinY(rs.getDouble("miny"));
                            u.setMaxX(rs.getDouble("maxx"));
                            u.setMaxY(rs.getDouble("maxy"));
                            u.setOriginX(rs.getDouble("origin_x"));
                            u.setOriginY(rs.getDouble("origin_y"));

                            u.setMinZoom(rs.getInt("min_zoom"));
                            u.setMaxZoom(rs.getInt("max_zoom"));

                            u.setWidth(rs.getInt("width"));
                            u.setHeight(rs.getInt("height"));
                            u.setMimeType(rs.getString("mime_type"));
                            u.setExtension(rs.getString("extension"));

                            u.setKind(rs.getInt("kind"));
                            u.setSource(rs.getString("source"));
                            u.setFileExtension(rs.getString("file_extension"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
    public TileMap getTileMapById(long mapId){
        try{
            String sql="select * from t_tilemap where id=?";
            List<TileMap> list=jdbcTemplate.query(
                    sql,
                    new Object[]{
                            mapId
                    },
                    new int[]{
                            Types.BIGINT
                    },
                    new RowMapper<TileMap>() {
                        public TileMap mapRow(ResultSet rs, int i) throws SQLException {
                            TileMap u=new TileMap();
                            u.setId(rs.getLong("id"));
                            u.setServiceId(rs.getLong("service_id"));
                            u.setTitle(rs.getString("title"));
                            u.setAbstract(rs.getString("abstract"));
                            u.setSrs(rs.getString("srs"));
                            u.setProfile(rs.getString("profile"));
                            u.setHref(rs.getString("href"));

                            u.setMinX(rs.getDouble("minx"));
                            u.setMinY(rs.getDouble("miny"));
                            u.setMaxX(rs.getDouble("maxx"));
                            u.setMaxY(rs.getDouble("maxy"));
                            u.setOriginX(rs.getDouble("origin_x"));
                            u.setOriginY(rs.getDouble("origin_y"));

                            u.setWidth(rs.getInt("width"));
                            u.setHeight(rs.getInt("height"));
                            u.setMimeType(rs.getString("mime_type"));
                            u.setExtension(rs.getString("extension"));

                            u.setKind(rs.getInt("kind"));
                            u.setSource(rs.getString("source"));
                            u.setFileExtension(rs.getString("file_extension"));
                            return u;
                        }
                    });
            if(list.size()>0)return list.get(0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public TileMap getTileMapById(long serviceId,String title, String srs, String extension){
        try{
            String sql="select * from t_tilemap where service_id=? and title=? and srs=? and extension=?";
            List<TileMap> list=jdbcTemplate.query(
                    sql,
                    new Object[]{
                            serviceId,
                            title,
                            srs,
                            extension
                    },
                    new int[]{
                            Types.BIGINT,
                            Types.VARCHAR,
                            Types.VARCHAR,
                            Types.VARCHAR,
                    },
                    new RowMapper<TileMap>() {
                        public TileMap mapRow(ResultSet rs, int i) throws SQLException {
                            TileMap u=new TileMap();
                            u.setId(rs.getLong("id"));
                            u.setServiceId(rs.getLong("service_id"));
                            u.setTitle(rs.getString("title"));
                            u.setAbstract(rs.getString("abstract"));
                            u.setSrs(rs.getString("srs"));
                            u.setProfile(rs.getString("profile"));
                            u.setHref(rs.getString("href"));

                            u.setMinX(rs.getDouble("minx"));
                            u.setMinY(rs.getDouble("miny"));
                            u.setMaxX(rs.getDouble("maxx"));
                            u.setMaxY(rs.getDouble("maxy"));
                            u.setOriginX(rs.getDouble("origin_x"));
                            u.setOriginY(rs.getDouble("origin_y"));

                            u.setWidth(rs.getInt("width"));
                            u.setHeight(rs.getInt("height"));
                            u.setMimeType(rs.getString("mime_type"));
                            u.setExtension(rs.getString("extension"));

                            u.setKind(rs.getInt("kind"));
                            u.setSource(rs.getString("source"));
                            u.setFileExtension(rs.getString("file_extension"));
                            return u;
                        }
                    });
            if(list.size()>0)return list.get(0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public List<TileSet> getTileSetList(long mapId){
        List<TileSet> list=null;
        try{
            String sql="select * from t_tileset where map_id=? order by sort_order";
            list=jdbcTemplate.query(
                    sql,
                    new Object[]{mapId},
                    new int[]{Types.INTEGER},
                    new RowMapper<TileSet>() {
                        public TileSet mapRow(ResultSet rs, int i) throws SQLException {
                            TileSet u=new TileSet();
                            u.setId(rs.getLong("id"));
                            u.setMapId(rs.getLong("map_id"));
                            u.setHref(rs.getString("href"));
                            u.setUnitsPerPixel(rs.getString("units_per_pixel"));
                            u.setSortOrder(rs.getString("sort_order"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
}
