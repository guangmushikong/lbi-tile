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

}
