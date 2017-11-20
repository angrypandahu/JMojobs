package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.ResumeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Resume and its DTO ResumeDTO.
 */
@Mapper(componentModel = "spring", uses = {BasicInformationMapper.class, UserMapper.class})
public interface ResumeMapper extends EntityMapper<ResumeDTO, Resume> {

    @Mapping(source = "basicInformation.id", target = "basicInformationId")
    @Mapping(source = "basicInformation.firstName", target = "basicInformationFirstName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ResumeDTO toDto(Resume resume); 

    @Mapping(source = "basicInformationId", target = "basicInformation")
    @Mapping(target = "experiencies", ignore = true)
    @Mapping(target = "educationBackgrounds", ignore = true)
    @Mapping(target = "professionalDevelopments", ignore = true)
    @Mapping(target = "languages", ignore = true)
    @Mapping(source = "userId", target = "user")
    Resume toEntity(ResumeDTO resumeDTO);

    default Resume fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resume resume = new Resume();
        resume.setId(id);
        return resume;
    }
}
