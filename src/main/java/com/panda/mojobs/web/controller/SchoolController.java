package com.panda.mojobs.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/school")
public class SchoolController {

    @RequestMapping("")
    public String home() {

        return "school/school";
    }


}
