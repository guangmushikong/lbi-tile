package com.lbi.tile.controller;

import com.alibaba.fastjson.JSONObject;
import com.lbi.map.Tile;
import com.lbi.tile.model.ResultBody;
import com.lbi.tile.service.CityService;

import com.lbi.util.ImageUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/city")
public class CityController {
    @Resource(name="cityService")
    private CityService cityService;

    @RequestMapping(value="/getcitylist.json",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody getCityList() {
        List<Map<String,String>> list=cityService.getCityList();
        return new ResultBody<>(list);
    }

    @RequestMapping(value="/city/{z}/{x}/{y}.json",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    List<JSONObject> getCityRegionByTile(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        return cityService.getCityRegionByTile(tile);
    }

    @RequestMapping(value="/gujiao/{z}/{x}/{y}.png",method = RequestMethod.GET)
    public ResponseEntity gujiao(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        BufferedImage image=cityService.getGujiao(tile);
        byte[] bytes=null;
        if(image!=null)bytes=ImageUtil.toByteArray(image);
        else bytes=ImageUtil.emptyImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
    }

    @RequestMapping(value="/world/{z}/{x}/{y}.png",method = RequestMethod.GET)
    public ResponseEntity world(
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        BufferedImage image=cityService.getWorld(tile);
        byte[] bytes=null;
        if(image!=null)bytes=ImageUtil.toByteArray(image);
        else bytes=ImageUtil.emptyImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }

}
