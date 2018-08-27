package com.lbi.tile.controller;

import com.lbi.tile.model.xml.Root_Services;
import com.lbi.tile.model.xml.Root_TileMap;
import com.lbi.tile.model.xml.Root_TileMapService;
import com.lbi.tile.service.MetaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
public class MetaController {
    @Resource(name="metaService")
    MetaService metaService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity getService() {
        Root_Services u = metaService.getServices();
        if (u != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(u);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/{service}/{version}", method = RequestMethod.GET)
    public ResponseEntity getTileMapService(
            @PathVariable("service") String service,
            @PathVariable("version") String version) {
        System.out.println("version:"+version);
        Root_TileMapService u=null;
        if(service.equalsIgnoreCase("xyz")){
            u = metaService.getTileMapService(1,version);
        }else if(service.equalsIgnoreCase("tms")){
            u = metaService.getTileMapService(2,version);
        }

        if (u != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(u);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/{service}/{version}/{tileset}", method = RequestMethod.GET)
    public ResponseEntity getTileMap(
            @PathVariable("service") String service,
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset) {
        String[] args=tileset.split("@");
        Root_TileMap u = null;
        if(service.equalsIgnoreCase("xyz")){
            u = metaService.getTileMap(1,version,args[0],args[1],args[2]);
        }else if(service.equalsIgnoreCase("tms")){
            u = metaService.getTileMap(2,version,args[0],args[1],args[2]);
        }

        if (u != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(u);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
}
