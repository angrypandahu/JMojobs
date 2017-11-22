package com.panda.mojobs.web.controller;

import com.panda.mojobs.service.MjobService;
import com.panda.mojobs.service.dto.MjobDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class HomeController {
    private MjobService mjobService;

    public HomeController(MjobService mjobService) {
        this.mjobService = mjobService;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String home(@RequestParam(required = false,defaultValue = "") String query, @PageableDefault(value = 1, sort = { "id" }, direction = Sort.Direction.DESC)  Pageable pageable, Model model) {
        Page<MjobDTO> mjobDTOPage = mjobService.findAll(pageable);
        model.addAttribute("page",mjobDTOPage);
        return "home/home";
    }

    @RequestMapping(value = "", method = {RequestMethod.POST})
    @ResponseBody
    public String homePost(HttpServletRequest request) {
        String method = request.getMethod();

        System.out.println("request--->" + method);
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (String s : parameterMap.keySet()) {
            String[] strings = parameterMap.get(s);
            for (String oneValue : strings) {
                System.out.println("key:" + s + " -->  " + oneValue);
            }
        }
        return "success";
    }
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("name", "Dear");
        return "hello";
    }

}
