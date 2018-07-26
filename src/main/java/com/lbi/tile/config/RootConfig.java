package com.lbi.tile.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.lbi.tile.model.TileMap;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RootConfig {
    @Autowired
    Environment env;
    @Value("${spring.table.t_tilemap}")
    String t_tilemap;

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        try{
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
            dataSource.setUrl(env.getProperty("spring.datasource.url"));
            dataSource.setUsername(env.getProperty("spring.datasource.username"));
            dataSource.setPassword(env.getProperty("spring.datasource.password"));
            dataSource.setMinIdle(10);
            dataSource.setMaxIdle(100);
            dataSource.setInitialSize(10);
            dataSource.setMaxActive(100);
            jdbcTemplate.setDataSource(dataSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jdbcTemplate;
    }


    @Bean(name = "ossClient")
    public OSSClient getOSSClient(){
        System.out.println("init ossClient");
        return new OSSClient(
                env.getProperty("oss.endpoint"),
                env.getProperty("oss.accessKeyId"),
                env.getProperty("oss.accessKeySecret"));

    }

    @Bean(name = "coverage_gujiao")
    public GridCoverage2D getGridCoverage2D_gujiao(){
        GridCoverage2D coverage=null;
        String localPath=env.getProperty("dem.gujiao");
        try{
            GeoTiffReader tifReader = new GeoTiffReader(localPath);
            coverage = tifReader.read(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("load gujiao DEM");
        return coverage;
    }
    @Bean(name = "coverage_jingzhuang")
    public GridCoverage2D getGridCoverage2D_jingzhuang(){
        GridCoverage2D coverage=null;
        String localPath=env.getProperty("dem.jingzhuang");
        try{
            GeoTiffReader tifReader = new GeoTiffReader(localPath);
            coverage = tifReader.read(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("load jingzhuang DEM");
        return coverage;
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
        myProps.setMapServer(env.getProperty("service.mapserver"));
        myProps.setGeoServer(env.getProperty("service.geoserver"));
        myProps.setTiledata(env.getProperty("service.tiledata"));
        return myProps;
    }

    private boolean syncDEMData(String localPath){
        boolean result=true;
        File file=new File(localPath);
        if(!file.exists()){
            System.out.println("sync oss data");
            OSSClient client=new OSSClient(
                    env.getProperty("oss.endpoint"),
                    env.getProperty("oss.accessKeyId"),
                    env.getProperty("oss.accessKeySecret"));
            String bucket=env.getProperty("dem.oss.bucket");
            String ossPath=env.getProperty("dem.oss.path");
            try{
                boolean found = client.doesObjectExist(bucket, ossPath);
                if(found){
                    OSSObject ossObject = client.getObject(bucket, ossPath);
                    InputStream in = ossObject.getObjectContent();
                    int index;
                    byte[] bytes = new byte[1024];
                    FileOutputStream downloadFile = new FileOutputStream(localPath);
                    while ((index = in.read(bytes)) != -1) {
                        downloadFile.write(bytes, 0, index);
                        downloadFile.flush();
                    }
                    downloadFile.close();
                    in.close();
                }else result= false;
            }catch (Exception e){
                e.printStackTrace();
                result= false;
            }
            client.shutdown();
        }
        return result;
    }

    private List<TileMap> loadTileMap(long serviceId){
        JdbcTemplate jdbcTemplate=getJdbcTemplate();
        List<TileMap> list=null;
        try{
            String sql="select * from "+t_tilemap+" where service_id =? order by id";
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
