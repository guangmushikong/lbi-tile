package com.lbi.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.lbi.web.service.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
@RequestMapping("/data")
public class DataController {
    @Resource(name="dataService")
    protected DataService dataService;

    @RequestMapping(value="/getcitylist",method = RequestMethod.GET)
    public void getCityList(HttpServletResponse resp){
        resp.setHeader("Access-Control-Allow-Methods","GET");
        resp.setContentType("text/html;charset=utf-8");
        try{
            JSONObject obj=dataService.getCityList();
            PrintWriter pw=resp.getWriter();
            pw.write(obj.toString());
            pw.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
