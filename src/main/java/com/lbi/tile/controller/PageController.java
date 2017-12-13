package com.lbi.tile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {
    @RequestMapping("/test")
    public String index(Model model) {
        return "test";
    }
    @RequestMapping("/openlayers_demo")
    public String openlayers_demo(Model model) {
        return "openlayers_demo";
    }
    @RequestMapping("/vectortile")
    public String vectortile(Model model) {
        return "vectortile";
    }
}
