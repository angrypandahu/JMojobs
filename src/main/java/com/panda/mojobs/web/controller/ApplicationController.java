package com.panda.mojobs.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/application")
public class ApplicationController {

    @RequestMapping("")
    public String home() {

        return "application/application";
    }


}
