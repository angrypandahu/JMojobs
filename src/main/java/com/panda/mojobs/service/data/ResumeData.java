package com.panda.mojobs.service.data;

import com.panda.mojobs.service.dto.BasicInformationDTO;
import com.panda.mojobs.service.dto.ExperienceDTO;
import com.panda.mojobs.service.dto.ResumeDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hupanpan on  2017/11/22.
 */
public class ResumeData implements Serializable{
    private ResumeDTO resumeDTO;
    private BasicInformationDTO basicInformationDTO;
    private List<ExperienceDTO> experienceDTOList;

    public ResumeDTO getResumeDTO() {
        return resumeDTO;
    }

    public void setResumeDTO(ResumeDTO resumeDTO) {
        this.resumeDTO = resumeDTO;
    }

    public BasicInformationDTO getBasicInformationDTO() {
        return basicInformationDTO;
    }

    public void setBasicInformationDTO(BasicInformationDTO basicInformationDTO) {
        this.basicInformationDTO = basicInformationDTO;
    }

    public List<ExperienceDTO> getExperienceDTOList() {
        return experienceDTOList;
    }

    public void setExperienceDTOList(List<ExperienceDTO> experienceDTOList) {
        this.experienceDTOList = experienceDTOList;
    }
}
