package com.lbi.tile.config;

import com.lbi.tile.model.TileMap;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MyConfig {
    Map<String, TileMap> tileMaps = new HashMap<>();
    Map<String, TileMap> xyzMaps = new HashMap<>();

    JdbcTemplate jdbcTemplate;

    public MyConfig(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
        loadData();
    }

    public TileMap getTileMap(String key){
        if(key==null || tileMaps.isEmpty())return null;
        return tileMaps.get(key);
    }

    public TileMap getXYZMap(String key){
        if(key==null || xyzMaps.isEmpty())return null;
        return xyzMaps.get(key);
    }

    private void loadData(){
        //XYZ Map
        List<TileMap> xyzMapList=getTileMapList(1);
        for(int i=0;i<xyzMapList.size();i++){
            TileMap tileMap=xyzMapList.get(i);
            String key=tileMap.getTitle()+"@"+tileMap.getSrs()+"@"+tileMap.getExtension();
            xyzMaps.put(key,tileMap);
        }
        log.info("load XYZMap:"+xyzMaps.size());
        //Tile Map
        List<TileMap> tileMapList=getTileMapList(2);
        for(int i=0;i<tileMapList.size();i++){
            TileMap tileMap=tileMapList.get(i);
            String key=tileMap.getTitle()+"@"+tileMap.getSrs()+"@"+tileMap.getExtension();
            tileMaps.put(key,tileMap);
        }
        log.info("load TileMap:"+tileMaps.size());
    }

    private List<TileMap> getTileMapList(long serviceId){
        List<TileMap> list=null;
        try{
            String sql="select * from t_tilemap where service_id =? order by id";
            list=jdbcTemplate.query(
                    sql,
                    new Object[]{serviceId},
                    new int[]{Types.BIGINT},
                    new RowMapper<TileMap>() {
                        public TileMap mapRow(ResultSet rs, int i) throws SQLException {
                            TileMap u=new TileMap();
                            u.setId(rs.getLong("id"));
                            u.setServiceId(rs.getLong("service_id"));
                            u.setTitle(rs.getString("title"));
                            u.setRecordDate(rs.getString("record_date"));
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
                            u.setGroup(rs.getString("layer_group"));
                            u.setSource(rs.getString("source"));
                            u.setFileExtension(rs.getString("file_extension"));
                            return u;
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return list;
    }


}
