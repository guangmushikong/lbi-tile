package com.lbi.tile.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lbi.model.Tile;
import com.lbi.tile.service.XYZService;


import com.lbi.util.ImageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@CrossOrigin
@RestController
@RequestMapping("/xyz")
public class XYZController {
    @Resource(name="xyzService")
    private XYZService xyzService;

    @RequestMapping(value="/{version}/{tileset}/{x}/{y}/{z}.{extension}",method = RequestMethod.GET)
    public ResponseEntity getTile(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("z") int z,
            @PathVariable("extension") String extension) {

        Tile tile=new Tile(x,y,z);
        int alterY=new Double(Math.pow(2,z)).intValue()-1-y;
        tile.setY(alterY);
        String[] args=tileset.split("@");
        if(extension.equalsIgnoreCase("geojson")
                ||extension.equalsIgnoreCase("json")){
            tile=new Tile(x,y,z);
            String layerName=args[0];
            if(layerName.equalsIgnoreCase("china_city_polygon")){
                JSONArray body=xyzService.getCityRegionByTile(tile);
                if(body!=null)return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
            }else if(layerName.equalsIgnoreCase("liupanshui_extent_line")) {
                JSONArray body=xyzService.getLPSByTile("liupanshui_extent_line",tile);
                if(body!=null)return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
            }else if(layerName.equalsIgnoreCase("liupanshui_track_line")) {
                JSONArray body=xyzService.getLPSByTile("liupanshui_track_line",tile);
                if(body!=null)return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
            }else if(layerName.equalsIgnoreCase("liupanshui_point")) {
                JSONArray body=xyzService.getLPSByTile("liupanshui_point",tile);
                if(body!=null)return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
            }else{
                String body=xyzService.getCacheJsonTile(version,args[0],args[1],args[2],tile);
                if(body!=null)return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
            }
        }else if(extension.equalsIgnoreCase("png")){
            byte[] bytes=xyzService.getXYZ_Tile(version,args[0],args[1],args[2],tile);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
        }else if(extension.equalsIgnoreCase("jpeg")){
            byte[] bytes=xyzService.getXYZ_Tile(version,args[0],args[1],args[2],tile);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }else if(extension.equalsIgnoreCase("tif")){
            byte[] bytes=xyzService.getXYZ_Tile(version,args[0],args[1],args[2],tile);
            return ResponseEntity.ok().contentType(MediaType.valueOf("image/tif")).body(bytes);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/{layerName}/{x}/{y}/{z}.{extension}",method = RequestMethod.GET)
    public ResponseEntity getXYZ(
            @PathVariable("layerName") String layerName,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("z") int z,
            @PathVariable("extension") String extension) {
        //System.out.println(layerName+","+x+","+y+","+z);
        Tile tile=new Tile(x,y,z);
        if(extension.equalsIgnoreCase("json")){
            JSONArray body=null;
            if(layerName.equalsIgnoreCase("gujiao_contour50_line")){
                body=xyzService.getGujiaoContour50ByTile(tile);
            }else if(layerName.equalsIgnoreCase("gujiao_contour100_line")){
                body=xyzService.getGujiaoContour100ByTile(tile);
            }else if(layerName.equalsIgnoreCase("gujiao_contour200_line")){
                body=xyzService.getGujiaoContour200ByTile(tile);
            }else if(layerName.equalsIgnoreCase("city")){
                body=xyzService.getCityRegionByTile(tile);
            }else if(layerName.equalsIgnoreCase("liupanshui_extent_line")){
                body=xyzService.getLPSByTile("liupanshui_extent_line",tile);
            }else if(layerName.equalsIgnoreCase("liupanshui_point")){
                body=xyzService.getLPSByTile("liupanshui_point",tile);
            }else if(layerName.equalsIgnoreCase("liupanshui_track_line")){
                body=xyzService.getLPSByTile("liupanshui_track_line",tile);
            }
            if(body!=null)return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
        }else if(extension.equalsIgnoreCase("png")){
            if(layerName.equalsIgnoreCase("gujiao"))layerName="gujiao_satellite_raster";
            else if(layerName.equalsIgnoreCase("city"))layerName="china_city_polygon";
            int alterY=new Double(Math.pow(2,z)).intValue()-1-y;
            tile.setY(alterY);
            byte[] bytes=xyzService.getXYZ_Tile(layerName,extension,tile);
            if(bytes==null)bytes= ImageUtil.emptyImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
        }else if(extension.equalsIgnoreCase("jpeg")){
            if(layerName.equalsIgnoreCase("world"))layerName="world_satellite_raster";
            int alterY=new Double(Math.pow(2,z)).intValue()-1-y;
            tile.setY(alterY);
            byte[] bytes=xyzService.getXYZ_Tile(layerName,extension,tile);
            if(bytes==null)bytes= ImageUtil.emptyImage();
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

    @RequestMapping(value="/contour/{x}/{y}/{z}.json",method = RequestMethod.GET)
    public ResponseEntity getContour(
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("z") int z){
        Tile tile=new Tile(x,y,z);
        JSONObject body=xyzService.getJingZhuangContourByTile(tile);
        if(body!=null)return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

}
