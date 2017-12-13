package com.panda.mojobs.web.controller;

import com.panda.mojobs.service.BasicInformationService;
import com.panda.mojobs.service.ImageService;
import com.panda.mojobs.service.ResumeService;
import com.panda.mojobs.service.data.ResumeData;
import com.panda.mojobs.service.dto.BasicInformationDTO;
import com.panda.mojobs.service.dto.ImageDTO;
import com.panda.mojobs.service.dto.ResumeDTO;
import com.panda.mojobs.web.controller.util.EnumUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    final private ResumeService resumeService;
    final private BasicInformationService basicInformationService;
    final private ImageService imageService;

    public ResumeController(ResumeService resumeService, BasicInformationService basicInformationService, ImageService imageService) {
        this.resumeService = resumeService;
        this.basicInformationService = basicInformationService;
        this.imageService = imageService;
    }

    @RequestMapping("")
    public String myResume(Model model) {
        List<ResumeDTO> list = resumeService.findByCurrentUser();
        ResumeDTO resume;
        if (list != null && list.size() > 0) {
            resume = list.get(0);
        } else {
            resume = resumeService.createByLoginUser();
        }
        return "redirect:/resume/" + resume.getId();
    }

    @RequestMapping(value = "/{resumeId}/**", method = RequestMethod.GET)
    public String resume(@PathVariable("resumeId") final Long resumeId, Model model) {
        ResumeDTO resumeDTO = resumeService.findOne(resumeId);
        ResumeData resumeData = resumeService.toResumeData(resumeDTO);
        model.addAttribute("resumeData", resumeData);
        model.addAttribute("genders", EnumUtil.getGender());
        model.addAttribute("educationLevels", EnumUtil.getEducationLevel());
        return "resume/resume";
    }

    @RequestMapping(value = "/{resumeId}/basicInformation", method = {RequestMethod.POST})
    public String basicInformation(@Valid final BasicInformationDTO basicInformationDTO, final BindingResult bindingResult, @PathVariable("resumeId") final Long resumeId, Model model) {
        ResumeDTO resumeDTO = resumeService.findOne(resumeId);

        if (bindingResult.hasErrors()) {
            System.out.println("Error!");
            model.addAttribute("basicInformationDTO", basicInformationDTO);
        } else {
            BasicInformationDTO basicInformationDTOSaved = basicInformationService.save(basicInformationDTO);
            resumeDTO.setBasicInformationId(basicInformationDTOSaved.getId());
            resumeService.save(resumeDTO);
            model.addAttribute("basicInformationDTO", basicInformationDTOSaved);
        }
        Long imageId = basicInformationDTO.getImageId();
        ImageDTO imageDTO;
        if (imageId != null) {
            imageDTO = imageService.findOne(imageId);
        } else {
            imageDTO = new ImageDTO();
        }
        model.addAttribute("imageDTO", imageDTO);
        model.addAttribute("resumeDTO", resumeDTO);
        model.addAttribute("genders", EnumUtil.getGender());
        model.addAttribute("educationLevels", EnumUtil.getEducationLevel());
        return "resume/resume";
    }
}
