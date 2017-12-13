package com.lbi.tile.controller;


import com.lbi.map.Tile;
import com.lbi.tile.service.TMSService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/tms")
public class TMSController {
    @Resource(name="tmsService")
    private TMSService tmsService;


    @RequestMapping(value="/{version}/{tileset}/{z}/{x}/{y}.png",method = RequestMethod.GET)
    public ResponseEntity tms(
            @PathVariable("version") String version,
            @PathVariable("tileset") String tileset,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y) {
        Tile tile=new Tile(x,y,z);
        String[] args=tileset.split("@");
        byte[] bytes=tmsService.getTMS(tileset,tile);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
    }

}
