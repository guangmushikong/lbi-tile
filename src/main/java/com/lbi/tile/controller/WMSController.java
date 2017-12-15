package com.lbi.tile.controller;


import com.lbi.tile.service.WMSService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/wms")
public class WMSController {
    @Resource(name="wmsService")
    private WMSService wmsService;




}
