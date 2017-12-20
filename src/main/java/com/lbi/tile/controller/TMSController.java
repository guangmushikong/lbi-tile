package com.lbi.tile.controller;


import com.lbi.map.Tile;
import com.lbi.tile.model.xml.XmlRoot_TileMap;
import com.lbi.tile.model.xml.XmlRoot_TileMapService;
import com.lbi.tile.service.TMSService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/tms")
public class TMSController {
    @Resource(name="tmsService")
    private TMSService tmsService;

    @RequestMapping(value="/{version}", method = RequestMethod.GET,produces = MediaType.TEXT_XML_VALUE)
    public XmlRoot_TileMapService tileMapService(@PathVariable("version") String version) {
        XmlRoot_TileMapService u = tmsService.getTileMapService(version);
        return u;
    }
    @RequestMapping(value="/{version}/{tileset}", method = RequestMethod.GET,produces = MediaType.TEXT_XML_VALUE)
    @ResponseBody
    public XmlRoot_TileMap tileMap(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset) {
        String[] args=tileset.split("@");
        XmlRoot_TileMap u = tmsService.getTileMap(version,args[0],args[1],args[2]);
        return u;
    }

    @RequestMapping(value="/{version}/{tileset}/{z}/{x}/{y}.png",method = RequestMethod.GET)
    public ResponseEntity tms_png(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        String[] args=tileset.split("@");
        byte[] bytes=tmsService.getTMS(version,args[0],args[1],args[2],tile);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
    }
    @RequestMapping(value="/{version}/{tileset}/{z}/{x}/{y}.jpeg",method = RequestMethod.GET)
    public ResponseEntity tms_jpeg(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        String[] args=tileset.split("@");
        byte[] bytes=tmsService.getTMS(version,args[0],args[1],args[2],tile);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }
    @RequestMapping(value="/{version}/{tileset}/{z}/{x}/{y}.tif",method = RequestMethod.GET)
    public ResponseEntity tms_tif(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        String[] args=tileset.split("@");
        byte[] bytes=tmsService.getTMS(version,args[0],args[1],args[2],tile);
        return ResponseEntity.ok().contentType(MediaType.valueOf("image/tif")).body(bytes);
    }

}
