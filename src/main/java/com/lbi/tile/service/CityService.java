package com.lbi.tile.service;


import com.lbi.tile.dao.CityDao;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import java.util.*;

@Service("cityService")
public class CityService {
    @Resource(name="cityDao")
    private CityDao cityDao;


    public List<Map<String,String>> getCityList(){
        return cityDao.getCityList();
    }
}
