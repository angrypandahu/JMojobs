package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.ExperienceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Experience and its DTO ExperienceDTO.
 */
@Mapper(componentModel = "spring", uses = {ResumeMapper.class})
public interface ExperienceMapper extends EntityMapper<ExperienceDTO, Experience> {

    @Mapping(source = "resume.id", target = "resumeId")
    @Mapping(source = "resume.name", target = "resumeName")
    ExperienceDTO toDto(Experience experience); 

    @Mapping(source = "resumeId", target = "resume")
    Experience toEntity(ExperienceDTO experienceDTO);

    default Experience fromId(Long id) {
        if (id == null) {
            return null;
        }
        Experience experience = new Experience();
        experience.setId(id);
        return experience;
    }
}
