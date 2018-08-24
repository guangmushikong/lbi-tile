package com.lbi.tile.dao;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;


public class CommonDao {
    @Resource(name="jdbcTemplate")
    public JdbcTemplate jdbcTemplate;

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
    public int getTotal(String tableName, String whereSql){
        String sql="select count(*) as total from "+tableName;
        if(StringUtils.isNotEmpty(whereSql)){
            sql+=" where "+whereSql;
        }
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }
    public long nextVal(String sequenceName){
        String sql="select nextval('"+sequenceName+"')";
        return jdbcTemplate.queryForObject(sql,Long.class);
    }
}
