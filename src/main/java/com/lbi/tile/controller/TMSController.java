package com.lbi.tile.controller;

import com.lbi.map.Tile;
import com.lbi.tile.model.xml.XmlRoot_TileMap;
import com.lbi.tile.model.xml.XmlRoot_TileMapService;
import com.lbi.tile.service.TMSService;

import org.springframework.http.HttpStatus;
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
    public XmlRoot_TileMapService getTileMapService(
            @PathVariable("version") String version) {
        XmlRoot_TileMapService u = tmsService.getTileMapService(version);
        return u;
    }
    @RequestMapping(value="/{version}/{tileset}", method = RequestMethod.GET,produces = MediaType.TEXT_XML_VALUE)
    @ResponseBody
    public XmlRoot_TileMap getTileMap(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset) {
        String[] args=tileset.split("@");
        XmlRoot_TileMap u = tmsService.getTileMap(version,args[0],args[1],args[2]);
        return u;
    }

    @RequestMapping(value="/{version}/{tileset}/{z}/{x}/{y}.{extension}",method = RequestMethod.GET)
    public ResponseEntity getTile(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("extension") String extension) {
        Tile tile=new Tile(x,y,z);
        String[] args=tileset.split("@");
        if(extension.equalsIgnoreCase("json")){

        }else if(extension.equalsIgnoreCase("png")){
            byte[] bytes=tmsService.getTMS_Tile(version,args[0],args[1],args[2],tile);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
        }else if(extension.equalsIgnoreCase("jpeg")){
            byte[] bytes=tmsService.getTMS_Tile(version,args[0],args[1],args[2],tile);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }else if(extension.equalsIgnoreCase("tif")){
            byte[] bytes=tmsService.getTMS_Tile(version,args[0],args[1],args[2],tile);
            return ResponseEntity.ok().contentType(MediaType.valueOf("image/tif")).body(bytes);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
}
