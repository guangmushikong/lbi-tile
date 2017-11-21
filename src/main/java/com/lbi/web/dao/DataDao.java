package com.lbi.web.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.apache.commons.lang3.StringUtils.isNoneEmpty;


@Repository(value="dataDao")
public class DataDao {
    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取表记录总数
     * @param tableName 表名
     * @return 记录总数
     */
    public int getTotal(String tableName){
        String sql="select count(*) as total from "+tableName;
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }
    /**
     * 根据条件获取表记录总数
     * @param tableName 表名
     * @param whereSql 关键字
     * @return 记录总数
     */
    public int getTotal(String tableName,String whereSql){
        String sql="select count(*) as total from "+tableName;
        if(isNoneEmpty(whereSql)){
            sql+=" where "+whereSql;
        }
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

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
