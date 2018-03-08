package com.lbi.tile.service;

import com.lbi.tile.dao.LogDao;
import com.lbi.tile.dao.MetaDao;
import com.lbi.tile.model.Stat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service("logService")
public class LogService {
    @Resource(name="logDao")
    private LogDao logDao;
    final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyyMMdd");

    public List<Stat> getThisDayList(){
        Date tDate=new Date();
        String sql=getThisDayStat(tDate);
        return logDao.getLogStat(sql);
    }
    public List<Stat> getLastDayList(){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.DAY_OF_YEAR,-1);
        String sql=getThisDayStat(rightNow.getTime());
        return logDao.getLogStat(sql);
    }
    public List<Stat> getLast7DayList(){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.WEEK_OF_YEAR,-1);
        String sql=getPeriodStat(rightNow.getTime());
        return logDao.getLogStat(sql);
    }
    public List<Stat> getLast1MonthList(){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.MONTH,-1);
        String sql=getPeriodStat(rightNow.getTime());
        return logDao.getLogStat(sql);
    }
    private String getThisDayStat(Date tDate){
        long ds=Long.parseLong(DATEFORMAT.format(tDate));
        StringBuilder sb=new StringBuilder();
        sb.append("select ip,to_char(log_time,'HH') as time,count(1) as total");
        sb.append(" from t_log where to_char(log_time,'yyyymmdd')::bigint="+ds);
        String ipSql=getTopIP(ds,10,2);
        sb.append(" and ip in ("+ipSql+")");
        sb.append(" group by ip,to_char(log_time,'HH')");
        sb.append(" order by ip,to_char(log_time,'HH')");
        return sb.toString();
    }
    private String getPeriodStat(Date tDate){
        long ds=Long.parseLong(DATEFORMAT.format(tDate));
        StringBuilder sb=new StringBuilder();
        sb.append("select ip,to_char(log_time,'yyyymmdd') as time,count(1) as total");
        sb.append(" from t_log where to_char(log_time,'yyyymmdd')::bigint>="+ds);
        String ipSql=getTopIP(ds,10,1);
        sb.append(" and ip in ("+ipSql+")");
        sb.append(" group by ip,to_char(log_time,'yyyymmdd')");
        sb.append(" order by ip,to_char(log_time,'yyyymmdd')");
        return sb.toString();
    }

    private String getTopIP(long ds,int limit,int kind){
        StringBuilder sb=new StringBuilder();
        sb.append("select ip from t_log");
        if(kind==1){
            sb.append(" where to_char(log_time,'yyyymmdd')::bigint>="+ds);
        }else{
            sb.append(" where to_char(log_time,'yyyymmdd')::bigint="+ds);
        }
        sb.append(" and ip not in ('0:0:0:0:0:0:0:1','223.71.139.186','223.71.139.187','223.71.139.188','223.71.139.189')");
        sb.append(" group by ip");
        sb.append(" order by count(1) desc limit 10");
        return sb.toString();
    }
}
