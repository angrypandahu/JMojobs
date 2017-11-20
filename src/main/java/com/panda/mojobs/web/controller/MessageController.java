package com.panda.mojobs.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
public class MessageController {

    @RequestMapping("")
    public String home() {

        return "message/message";
    }


}
