package com.lbi.tile.controller;


import com.alibaba.fastjson.JSONArray;
import com.lbi.map.Tile;
import com.lbi.tile.service.XYZService;
import com.lbi.util.ImageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/xyz")
public class XYZController {
    @Resource(name="xyzService")
    private XYZService xyzService;


    @RequestMapping(value="/{layerName}/{x}/{y}/{z}.{extension}",method = RequestMethod.GET)
    public ResponseEntity xyz(
            @PathVariable("layerName") String layerName,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("z") int z,
            @PathVariable("extension") String extension) {
        Tile tile=new Tile(x,y,z);
        if(extension.equalsIgnoreCase("json")){
            JSONArray body=xyzService.getCityRegionByTile(tile);
            return new ResponseEntity<JSONArray>(body, HttpStatus.OK);
        }else if(extension.equalsIgnoreCase("png")){
            if(layerName.equalsIgnoreCase("gujiao"))layerName="gujiao_satellite_raster";
            else if(layerName.equalsIgnoreCase("city"))layerName="china_city_polygon";
            int alterY=new Double(Math.pow(2,z)).intValue()-1-y;
            tile.setY(alterY);
            byte[] bytes=xyzService.getXYZ_Tile(layerName,extension,tile);
            if(bytes==null)bytes=ImageUtil.emptyImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
        }else if(extension.equalsIgnoreCase("jpeg")){
            if(layerName.equalsIgnoreCase("world"))layerName="world_satellite_raster";
            int alterY=new Double(Math.pow(2,z)).intValue()-1-y;
            tile.setY(alterY);
            byte[] bytes=xyzService.getXYZ_Tile(layerName,extension,tile);
            if(bytes==null)bytes=ImageUtil.emptyImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }else if(extension.equalsIgnoreCase("tif")){
            int alterY=new Double(Math.pow(2,z)).intValue()-1-y;
            tile.setY(alterY);
            byte[] bytes=xyzService.getXYZ_Tile(layerName,extension,tile);
            if(bytes==null)bytes=ImageUtil.emptyImage();
            return ResponseEntity.ok().contentType(MediaType.valueOf("image/tif")).body(bytes);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

}
