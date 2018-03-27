package com.lbi.tile.service;

import com.lbi.tile.dao.LogDao;
import com.lbi.tile.model.Stat;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class readLog {
    @Resource(name="logDao")
    private LogDao logDao;
    @Resource(name="logService")
    private LogService logService;
    final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Test
    public void txt2db(){
        String fileName="D:/dev/logs/lbi-tile_output.2018-03-08.log";
        readLog(fileName);
    }
    @Test
    public void getTopIpList(){
        List<String> filterIPList=new ArrayList<>();
        filterIPList.add("0:0:0:0:0:0:0:1");
        filterIPList.add("223.71.139.186");
        filterIPList.add("223.71.139.187");
        filterIPList.add("223.71.139.188");
        filterIPList.add("223.71.139.189");
        List<String> ipList=logDao.getTopIpList(1,20180301,3,filterIPList);
        for(String ip:ipList){
            System.out.println(ip);
        }
    }
    @Test
    public void testStat(){
        List<Stat> list=logService.getLast7DayList();
        for(Stat stat:list){
            System.out.println(stat.getName()+","+stat.getPeriod()+","+stat.getTotal());
        }
    }
    private void readLog(String fileName){
        try{
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(fileName)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = null;
            long n=0;
            while ((line = br.readLine()) != null) {
                String[] args=line.split(" - ");
                if(args.length==5){
                    n++;
                    //System.out.println(n+"|"+line+"|"+args.length);
                    String logTime=args[0];
                    Date fTime=TIMEFORMAT.parse(logTime);
                    String ip=args[1];
                    ip=ip.replaceFirst("ip=","");
                    String message=args[2];
                    String useTime=args[3];
                    useTime=useTime.replaceFirst("usetime=","");
                    useTime=useTime.replace("ms","");
                    if(StringUtils.isNoneEmpty(useTime)){
                        Long fUseTime=Long.parseLong(useTime);
                        String method=args[4];
                        method=method.replaceFirst("method=","");
                        System.out.println(n+"|"+logTime+"|"+ip+"|"+message+"|"+fUseTime+"|"+method);
                        logDao.addLog(fTime,ip,message,method,fUseTime);
                    }else{
                        System.out.println(line);
                    }


                }
            }
            br.close();
            System.out.println("total:"+n);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
