package com.panda.mojobs.web.controller;

import com.panda.mojobs.service.MjobService;
import com.panda.mojobs.service.dto.MjobDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final MjobService mjobService;

    @Autowired
    public HomeController(MjobService mjobService) {
        this.mjobService = mjobService;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String home(@RequestParam(required = false, defaultValue = "") String query, @ApiParam Pageable pageable, Model model) {
        Page<MjobDTO> page = mjobService.findAll(pageable);
        model.addAttribute("page", page);
        return "home/home";
    }


}
