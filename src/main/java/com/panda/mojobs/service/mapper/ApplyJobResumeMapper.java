package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.ApplyJobResumeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApplyJobResume and its DTO ApplyJobResumeDTO.
 */
@Mapper(componentModel = "spring", uses = {ResumeMapper.class, MjobMapper.class})
public interface ApplyJobResumeMapper extends EntityMapper<ApplyJobResumeDTO, ApplyJobResume> {

    @Mapping(source = "resume.id", target = "resumeId")
    @Mapping(source = "resume.name", target = "resumeName")
    @Mapping(source = "mjob.id", target = "mjobId")
    @Mapping(source = "mjob.name", target = "mjobName")
    ApplyJobResumeDTO toDto(ApplyJobResume applyJobResume); 

    @Mapping(source = "resumeId", target = "resume")
    @Mapping(source = "mjobId", target = "mjob")
    ApplyJobResume toEntity(ApplyJobResumeDTO applyJobResumeDTO);

    default ApplyJobResume fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApplyJobResume applyJobResume = new ApplyJobResume();
        applyJobResume.setId(id);
        return applyJobResume;
    }
}
