package com.lbi.tile.controller;

import com.lbi.tile.model.ResultBody;
import com.lbi.tile.service.CityService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
@CrossOrigin
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
