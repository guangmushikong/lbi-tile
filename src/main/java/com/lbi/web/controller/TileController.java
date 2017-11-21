package com.lbi.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.lbi.web.filter.PropertyPlaceholder;
import com.lbi.web.model.Tile;
import com.lbi.web.service.TileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/tile")
public class TileController {
    @Resource(name="tileService")
    protected TileService tileService;

    static BufferedImage BLANKIMG;
    public TileController(){
        try{
            URL file=this.getClass().getClassLoader().getResource("none.png");
            BLANKIMG=ImageIO.read(file);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @RequestMapping(value="/gujiao/{z}/{x}/{y}",method = RequestMethod.GET)
    public void gujiao(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            HttpServletResponse resq){
        resq.setContentType("image/png");
        try {
            String fileName=PropertyPlaceholder.getProperty("gujiao.file")+"/"+z+"/"+x+"/"+y+".png";
            File file=new File(fileName);
            BufferedImage image;
            if(file.exists())image= ImageIO.read(file);
            else image=BLANKIMG;
            ServletOutputStream out = resq.getOutputStream();
            ImageIO.write(image, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value="/world/{z}/{x}/{y}",method = RequestMethod.GET)
    public void world(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            HttpServletResponse resq){
        resq.setContentType("image/png");
        try {
            String fileName=PropertyPlaceholder.getProperty("world.file")+"/"+z+"/"+x+"/"+y+".png";
            File file=new File(fileName);
            BufferedImage image;
            if(file.exists())image= ImageIO.read(file);
            else image=BLANKIMG;
            ServletOutputStream out = resq.getOutputStream();
            ImageIO.write(image, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value="/city/{x}/{y}/{z}",method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getCityRegionGeoJson(
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("z") int z){
        Tile tile=new Tile(x,y,z);
        return tileService.getCityRegionByTile(tile);
    }
}
