package com.lbi.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class PageController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello world!";
    }


}
