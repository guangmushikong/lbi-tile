package com.lbi.tile.config;

import com.aliyun.oss.OSSClient;
import com.lbi.tile.model.TileMap;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RootConfig {
    @Value("${spring.datasource.url}")
    String jdbc_url;
    @Value("${spring.datasource.username}")
    String jdbc_username;
    @Value("${spring.datasource.password}")
    String jdbc_password;
    @Value("${oss.accessKeyId}")
    String oss_accessKeyId;
    @Value("${oss.accessKeySecret}")
    String oss_accessKeySecret;
    @Value("${oss.endpoint}")
    String oss_endpoint;
    @Value("${service.mapserver}")
    String mapserver;
    @Value("${service.geoserver}")
    String geoserver;
    @Value("${service.tiledata}")
    String tiledata;



    @Bean(name = "dataSource")
    public BasicDataSource getDataSource() {
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl(jdbc_url);
            dataSource.setUsername(jdbc_username);
            dataSource.setPassword(jdbc_password);
            dataSource.setMinIdle(10);
            dataSource.setMaxIdle(100);
            dataSource.setInitialSize(10);
            dataSource.setMaxActive(100);
            return dataSource;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        jdbcTemplate.setDataSource(getDataSource());
        return jdbcTemplate;
    }
    @Bean(name = "myProps")
    public MyProps getMyProps(){
        MyProps myProps=new MyProps();
        //XYZ Map
        List<TileMap> xyzMapList=loadTileMap(1);
        Map<String, TileMap> xyzMapDict = new HashMap<>();
        for(int i=0;i<xyzMapList.size();i++){
            TileMap tileMap=xyzMapList.get(i);
            String key=tileMap.getTitle()+"@"+tileMap.getSrs()+"@"+tileMap.getExtension();
            xyzMapDict.put(key,tileMap);
        }
        System.out.println("load XYZMap:"+xyzMapDict.size());
        myProps.setXYZMapList(xyzMapDict);
        //Tile Map
        List<TileMap> tileMapList=loadTileMap(2);
        Map<String, TileMap> tileMapDict = new HashMap<>();
        for(int i=0;i<tileMapList.size();i++){
            TileMap tileMap=tileMapList.get(i);
            String key=tileMap.getTitle()+"@"+tileMap.getSrs()+"@"+tileMap.getExtension();
            tileMapDict.put(key,tileMap);
        }
        System.out.println("load TileMap:"+tileMapList.size());
        myProps.setTileMapList(tileMapDict);
        myProps.setMapServer(mapserver);
        myProps.setGeoServer(geoserver);
        myProps.setTiledata(tiledata);
        return myProps;
    }
    @Bean(name = "ossClient")
    public OSSClient getOSSClient(){
        return new OSSClient(oss_endpoint, oss_accessKeyId, oss_accessKeySecret);
    }

    private List<TileMap> loadTileMap(long serviceId){
        JdbcTemplate jdbcTemplate=getJdbcTemplate();
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
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

}
