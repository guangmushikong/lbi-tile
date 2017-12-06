package com.lbi.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository(value="dataDao")
public class DataDao {
    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

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
        if(StringUtils.isNoneEmpty(whereSql)){
            sql+=" where "+whereSql;
        }
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }
}
