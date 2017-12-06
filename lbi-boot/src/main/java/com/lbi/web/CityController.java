package com.lbi.web;

import com.alibaba.fastjson.JSONObject;
import com.lbi.map.Tile;
import com.lbi.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/city")
@EnableAutoConfiguration
public class CityController {
    @Autowired
    private CityService cityService;

    private static BufferedImage BLANKIMG;

    @Value("${gujiao.file}")
    private String GUJIAO_PATH;
    @Value("${world.file}")
    private String WORLD_PATH;

    public CityController(){
        try{
            Resource resource = new ClassPathResource("none.png");
            BLANKIMG= ImageIO.read(resource.getInputStream());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @RequestMapping(value="/getcitylist",method = RequestMethod.GET)
    @ResponseBody
    JSONObject getCityList() {
        return cityService.getCityList();
    }
    @RequestMapping(value="/city/{z}/{x}/{y}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    List<JSONObject> getCityRegionByTile(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        return cityService.getCityRegionByTile(tile);
    }

    /*@RequestMapping(value="/citymvt/{z}/{x}/{y}",method = RequestMethod.GET,produces = "application/x-protobuf")
    @ResponseBody
    byte[] getCityMVTByTile(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        return cityService.getCityMVTByTile(tile);
    }

    @RequestMapping(value="/poimvt/{z}/{x}/{y}",method = RequestMethod.GET,produces = "application/x-protobuf")
    @ResponseBody
    byte[] getPoiMVTByTile(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        return cityService.getPoiMVTByTile(tile);
    }*/

    @RequestMapping(value="/gujiao/{z}/{x}/{y}",method = RequestMethod.GET)
    public void gujiao(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            HttpServletResponse resq) {
        resq.setContentType("image/png");
        BufferedImage image=null;
        try{
            String fileName= GUJIAO_PATH+"/"+z+"/"+x+"/"+y+".png";
            File file=new File(fileName);
            if(file.exists())image= ImageIO.read(file);
            else image=BLANKIMG;
            ServletOutputStream out = resq.getOutputStream();
            ImageIO.write(image, "png", out);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @RequestMapping(value="/world/{z}/{x}/{y}",method = RequestMethod.GET)
    public void world(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            HttpServletResponse resq) {
        resq.setContentType("image/png");
        BufferedImage image=null;
        try{
            String fileName= WORLD_PATH+"/"+z+"/"+x+"/"+y+".jpg";
            System.out.println(fileName);
            File file=new File(fileName);
            if(file.exists())image= ImageIO.read(file);
            else image=BLANKIMG;
            ServletOutputStream out = resq.getOutputStream();
            ImageIO.write(image, "png", out);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CityController.class, args);
    }
}
