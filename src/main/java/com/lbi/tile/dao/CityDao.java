package com.lbi.tile.dao;


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
}
