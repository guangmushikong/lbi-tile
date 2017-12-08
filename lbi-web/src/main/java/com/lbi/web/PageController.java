package com.lbi.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class PageController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello world!";
    }
    @RequestMapping("/openlayers_demo")
    public ModelAndView openlayers_demo(){
        ModelAndView mav=new ModelAndView();
        mav.setViewName("forward:/jsp/openlayers_demo.jsp");
        return mav;
    }

    @RequestMapping("/demo")
    public ModelAndView demo(){
        ModelAndView mav=new ModelAndView();
        mav.setViewName("forward:/jsp/demo.jsp");
        return mav;
    }
}
