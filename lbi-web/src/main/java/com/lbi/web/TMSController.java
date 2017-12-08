package com.lbi.web;

import com.alibaba.fastjson.JSONObject;
import com.lbi.map.Tile;
import com.lbi.service.CityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/tms")
public class TMSController {

    @RequestMapping(value="/{version}")
    public ModelAndView tms(
            @PathVariable("version") String version){
        String url="http://54.223.166.139:8888/geoserver/gwc/service/tms/"+version;
        return new ModelAndView("redirect:"+url);
    }
    @RequestMapping(value="/{version}/{tileset}")
    public ModelAndView tms(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset){
        String url="http://54.223.166.139:8888/geoserver/gwc/service/tms/"+version+"/"+tileset;
        return new ModelAndView("redirect:"+url);
    }
    @RequestMapping(value="/{version}/{tileset}/{z}/{x}/{y}")
    public ModelAndView tms(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y){
        String url="http://54.223.166.139:8888/geoserver/gwc/service/tms/"+version+"/"+tileset+"/"+z+"/"+x+"/"+y+".png";
        return new ModelAndView("redirect:"+url);
    }
}
