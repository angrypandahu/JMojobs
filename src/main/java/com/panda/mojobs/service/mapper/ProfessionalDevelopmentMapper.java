package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.ProfessionalDevelopmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProfessionalDevelopment and its DTO ProfessionalDevelopmentDTO.
 */
@Mapper(componentModel = "spring", uses = {ResumeMapper.class})
public interface ProfessionalDevelopmentMapper extends EntityMapper<ProfessionalDevelopmentDTO, ProfessionalDevelopment> {

    @Mapping(source = "resume.id", target = "resumeId")
    @Mapping(source = "resume.name", target = "resumeName")
    ProfessionalDevelopmentDTO toDto(ProfessionalDevelopment professionalDevelopment); 

    @Mapping(source = "resumeId", target = "resume")
    ProfessionalDevelopment toEntity(ProfessionalDevelopmentDTO professionalDevelopmentDTO);

    default ProfessionalDevelopment fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfessionalDevelopment professionalDevelopment = new ProfessionalDevelopment();
        professionalDevelopment.setId(id);
        return professionalDevelopment;
    }
}
