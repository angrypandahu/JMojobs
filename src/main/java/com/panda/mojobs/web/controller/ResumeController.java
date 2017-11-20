package com.panda.mojobs.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @RequestMapping("")
    public String resume() {

        return "resume/resume";
    }

}
