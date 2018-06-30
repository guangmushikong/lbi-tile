package com.lbi.tile.service;

import com.lbi.tile.dao.LogDao;
import com.lbi.tile.model.Stat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service("logService")
public class LogService {
    @Resource(name="logDao")
    private LogDao logDao;

    @Value("${spring.table.t_log}")
    String t_log;

    final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyyMMdd");

    public List<Stat> getThisDayList(){
        Date tDate=new Date();
        String sql=getThisDayStat(tDate);
        List<Stat> list=logDao.getLogStat(sql);

        return list;
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
        List<String> filterIPList=new ArrayList<>();
        filterIPList.add("0:0:0:0:0:0:0:1");
        filterIPList.add("223.71.139.186");
        filterIPList.add("223.71.139.187");
        filterIPList.add("223.71.139.188");
        filterIPList.add("223.71.139.189");
        List<String> ipList=logDao.getTopIpList(2,ds,3,filterIPList);
        List<Long> hourList=new ArrayList<>();
        for(long i=0;i<24;i++){
            hourList.add(i);
        }

        StringBuilder sb=new StringBuilder();
        sb.append("select t1.ip,t1.time,t2.total from (");
        sb.append(" select ip,regexp_split_to_table('"+ StringUtils.join(hourList,',')+"',',')::bigint as time");
        sb.append(" from (select regexp_split_to_table('"+StringUtils.join(ipList,',')+"',',') as ip) t");
        sb.append(") t1 left join (");
        sb.append(" select ip,to_char(log_time,'HH')::bigint as time,count(1) as total from "+t_log);
        sb.append(" where to_char(log_time,'yyyymmdd')::bigint="+ds);
        sb.append(" and ip in ('"+StringUtils.join(ipList,"','")+"')");
        sb.append(" group by ip,to_char(log_time,'HH')");
        sb.append(") t2 on t1.ip=t2.ip and t1.time=t2.time");

        return sb.toString();
    }
    private String getPeriodStat(Date tDate){
        long ds=Long.parseLong(DATEFORMAT.format(tDate));
        long cds=Long.parseLong(DATEFORMAT.format(new Date()));
        List<String> filterIPList=new ArrayList<>();
        filterIPList.add("0:0:0:0:0:0:0:1");
        filterIPList.add("223.71.139.186");
        filterIPList.add("223.71.139.187");
        filterIPList.add("223.71.139.188");
        filterIPList.add("223.71.139.189");
        List<String> ipList=logDao.getTopIpList(1,ds,3,filterIPList);
        List<Long> timeList=new ArrayList<>();
        for(long i=ds;i<cds;i++){
            timeList.add(i);
        }

        StringBuilder sb=new StringBuilder();
        sb.append("select t1.ip,t1.time,t2.total from (");
        sb.append(" select ip,regexp_split_to_table('"+ StringUtils.join(timeList,',')+"',',')::bigint as time");
        sb.append(" from (select regexp_split_to_table('"+StringUtils.join(ipList,',')+"',',') as ip) t");
        sb.append(") t1 left join (");
        sb.append(" select ip,to_char(log_time,'yyyymmdd')::bigint as time,count(1) as total from "+t_log);
        sb.append(" where to_char(log_time,'yyyymmdd')::bigint>="+ds);
        sb.append(" and ip in ('"+StringUtils.join(ipList,"','")+"')");
        sb.append(" group by ip,to_char(log_time,'yyyymmdd')");
        sb.append(") t2 on t1.ip=t2.ip and t1.time=t2.time");

        /*StringBuilder sb=new StringBuilder();
        sb.append("select ip,to_char(log_time,'yyyymmdd') as time,count(1) as total");
        sb.append(" from t_log where to_char(log_time,'yyyymmdd')::bigint>="+ds);
        String ipSql=getTopIP(ds,3,1);
        sb.append(" and ip in ("+ipSql+")");
        sb.append(" group by ip,to_char(log_time,'yyyymmdd')");
        sb.append(" order by ip,to_char(log_time,'yyyymmdd')");*/
        return sb.toString();
    }

    private String getTopIP(long ds,int limit,int kind){
        StringBuilder sb=new StringBuilder();
        sb.append("select ip from "+t_log);
        if(kind==1){
            sb.append(" where to_char(log_time,'yyyymmdd')::bigint>="+ds);
        }else{
            sb.append(" where to_char(log_time,'yyyymmdd')::bigint="+ds);
        }
        sb.append(" and ip not in ('0:0:0:0:0:0:0:1','223.71.139.186','223.71.139.187','223.71.139.188','223.71.139.189')");
        sb.append(" group by ip");
        sb.append(" order by count(1) desc limit "+limit);
        return sb.toString();
    }
}
