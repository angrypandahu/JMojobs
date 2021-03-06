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
        String method = request.getMethod();
        System.out.println("request--->" + method);
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String s : parameterMap.keySet()) {
            String[] strings = parameterMap.get(s);
            for (String oneValue : strings) {
                System.out.println("key:" + s + " -->  " + oneValue);
            }
        }
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
