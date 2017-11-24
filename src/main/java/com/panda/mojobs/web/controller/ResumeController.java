package com.panda.mojobs.web.controller;

import com.panda.mojobs.service.ResumeService;
import com.panda.mojobs.service.data.ResumeData;
import com.panda.mojobs.service.dto.ResumeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    private ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @RequestMapping("")
    public String resume(Model model) {
        List<ResumeData> resumeDataList = resumeService.findDataByCurrentUser();
        ResumeData resume;
        if (resumeDataList != null && resumeDataList.size() > 0) {
            resume = resumeDataList.get(0);
        } else {
            resume = new ResumeData();
        }
        model.addAttribute("resume", resume);
        return "resume/resume";
    }

}
