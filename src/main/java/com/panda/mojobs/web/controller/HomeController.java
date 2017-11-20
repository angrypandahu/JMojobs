package com.panda.mojobs.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class HomeController {

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String home(HttpServletRequest request) {


        return "home/home";
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String search(HttpServletRequest request) {

        return "home/home";
    }

}
