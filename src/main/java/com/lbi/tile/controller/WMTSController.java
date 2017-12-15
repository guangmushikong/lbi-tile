package com.lbi.tile.controller;


import com.lbi.map.Tile;
import com.lbi.tile.service.WMTSService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/wmts")
public class WMTSController {
    @Resource(name="wmtsService")
    private WMTSService wmtsService;




}
