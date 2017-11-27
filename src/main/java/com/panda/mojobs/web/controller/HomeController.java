package com.panda.mojobs.web.controller;

import com.panda.mojobs.service.MjobService;
import com.panda.mojobs.service.dto.MjobDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class HomeController {
    private MjobService mjobService;

    public HomeController(MjobService mjobService) {
        this.mjobService = mjobService;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String home(@RequestParam(required = false, defaultValue = "") String query, @RequestParam(value = "page", defaultValue = "0") Integer page,
                       @RequestParam(value = "size", defaultValue = "1") Integer size, @RequestParam(value = "sort", defaultValue = "id") String sortStr, Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, sortStr);
        Pageable pageable = new PageRequest(page, size, sort);
        Page<MjobDTO> datas = mjobService.findAll(pageable);
        String pageUrl = UriComponentsBuilder.fromUriString("").queryParam("query", query).queryParam("size", size).queryParam("sort", sortStr).toUriString();
        model.addAttribute("page", datas);
        model.addAttribute("pageUrl", pageUrl);
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
