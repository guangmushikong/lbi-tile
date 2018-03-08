package com.lbi.tile.dao;

import com.lbi.tile.model.Stat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Repository(value="logDao")
public class LogDao {
    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public int addLog(String ip,String message,String method,long usetime){
        String sql="insert into t_log(ip,message,method,usetime) values(?,?,?,?)";
        int result=0;
        try{
            result=jdbcTemplate.update(sql,
                    new Object[]{
                            ip,
                            message,
                            method,
                            usetime
                    },
                    new int[]{
                            Types.VARCHAR,
                            Types.VARCHAR,
                            Types.VARCHAR,
                            Types.BIGINT
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public int addLog(Date logTime, String ip, String message, String method, long usetime){
        String sql="insert into t_log(log_time,ip,message,method,usetime) values(?,?,?,?,?)";
        int result=0;
        try{
            result=jdbcTemplate.update(sql,
                    new Object[]{
                            logTime,
                            ip,
                            message,
                            method,
                            usetime
                    },
                    new int[]{
                            Types.TIMESTAMP,
                            Types.VARCHAR,
                            Types.VARCHAR,
                            Types.VARCHAR,
                            Types.BIGINT
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public List<Stat> getLogStat(String sql){
        List<Stat> list=null;
        try{
            System.out.println("sql:"+sql);
            list=jdbcTemplate.query(
                    sql,
                    new RowMapper<Stat>() {
                        public Stat mapRow(ResultSet rs, int i) throws SQLException {
                            Stat u=new Stat();
                            u.setName(rs.getString("ip"));
                            u.setPeriod(rs.getLong("time"));
                            u.setTotal(rs.getLong("total"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

}
