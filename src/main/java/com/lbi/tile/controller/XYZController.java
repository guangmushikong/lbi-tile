package com.lbi.tile.controller;


import com.alibaba.fastjson.JSONObject;
import com.lbi.map.Tile;
import com.lbi.map.TileSystem;
import com.lbi.tile.service.XYZService;
import com.lbi.util.ImageUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.List;


@RestController
@RequestMapping("/xyz")
public class XYZController {
    @Resource(name="xyzService")
    private XYZService xyzService;

    @RequestMapping(value="/city/{x}/{y}/{z}.json",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    List<JSONObject> getCityRegionByTile(
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("z") int z) {
        Tile tile=new Tile(x,y,z);
        return xyzService.getCityRegionByTile(tile);
    }

    @RequestMapping(value="/gujiao/{x}/{y}/{z}.png",method = RequestMethod.GET)
    public ResponseEntity gujiao(
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("z") int z) {
        Tile tile=new Tile(x,y,z);
        int alterY=new Double(Math.pow(2,z)).intValue()-1-y;
        tile.setY(alterY);
        byte[] bytes=xyzService.getGujiao(tile);
        if(bytes==null)bytes=ImageUtil.emptyImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
    }

    @RequestMapping(value="/world/{x}/{y}/{z}.jpeg",method = RequestMethod.GET)
    public ResponseEntity world(
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("z") int z) {
        Tile tile=new Tile(x,y,z);
        int alterY=new Double(Math.pow(2,z)).intValue()-1-y;
        tile.setY(alterY);
        byte[] bytes=xyzService.getWorld(tile);
        if(bytes==null)bytes=ImageUtil.emptyImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }
}
