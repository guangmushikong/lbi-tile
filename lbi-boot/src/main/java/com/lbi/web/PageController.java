package com.lbi.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello world!";
    }

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/test")
    public String test(Model model) {
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
