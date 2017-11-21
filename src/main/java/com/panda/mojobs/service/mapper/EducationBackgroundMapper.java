package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.EducationBackgroundDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EducationBackground and its DTO EducationBackgroundDTO.
 */
@Mapper(componentModel = "spring", uses = {ResumeMapper.class})
public interface EducationBackgroundMapper extends EntityMapper<EducationBackgroundDTO, EducationBackground> {

    @Mapping(source = "resume.id", target = "resumeId")
    @Mapping(source = "resume.name", target = "resumeName")
    EducationBackgroundDTO toDto(EducationBackground educationBackground); 

    @Mapping(source = "resumeId", target = "resume")
    EducationBackground toEntity(EducationBackgroundDTO educationBackgroundDTO);

    default EducationBackground fromId(Long id) {
        if (id == null) {
            return null;
        }
        EducationBackground educationBackground = new EducationBackground();
        educationBackground.setId(id);
        return educationBackground;
    }
}
