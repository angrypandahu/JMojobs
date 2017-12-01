package com.panda.mojobs.web.controller;

import com.panda.mojobs.domain.enumeration.Location;
import com.panda.mojobs.service.MjobService;
import com.panda.mojobs.service.dto.MjobDTO;
import com.panda.mojobs.web.controller.form.MjobSearchForm;
import com.panda.mojobs.web.controller.util.EnumUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class HomeController {
    private MjobService mjobService;

    public HomeController(MjobService mjobService) {
        this.mjobService = mjobService;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String home(MjobSearchForm mjobSearchForm,
                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                       @RequestParam(value = "size", defaultValue = "1") Integer size, @RequestParam(value = "sort", defaultValue = "id") String sortStr, Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, sortStr);
        Pageable pageable = new PageRequest(page, size, sort);
        Page<MjobDTO> datas = mjobService.findAll(mjobSearchForm, pageable);
        if (mjobSearchForm == null) {
            mjobSearchForm = new MjobSearchForm();
        }
        model.addAttribute("mjobSearchForm", mjobSearchForm);
        System.out.println(mjobSearchForm.getLocation());
        System.out.println(mjobSearchForm.getName());
        String pageUrl = UriComponentsBuilder.fromUriString("").queryParam("size", size).queryParam("sort", sortStr).toUriString();
        model.addAttribute("page", datas);
        model.addAttribute("pageUrl", pageUrl);
        model.addAttribute("locations", EnumUtil.getLocations());
        model.addAttribute("schoolTypes", EnumUtil.getSchoolType());
        model.addAttribute("schoolLevels", EnumUtil.getSchoolLevel());

        return "home/home";
    }





}
