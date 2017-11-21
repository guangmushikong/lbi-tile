package com.lbi.web.service;

import com.alibaba.fastjson.JSONObject;
import com.lbi.web.dao.DataDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("dataService")
public class DataService {
    @Resource(name="dataDao")
    private DataDao dataDao;

    public JSONObject getCityList(){
        JSONObject obj=new JSONObject();
        List<Map<String,String>> list=dataDao.getCityList();
        if(list!=null){
            obj.put("success",true);
            obj.put("msg","操作成功");
            obj.put("data",list);
        }else{
            obj.put("success",false);
            obj.put("msg","操作失败");
        }
        return obj;
    }
}
