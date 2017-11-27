package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.MLanguageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MLanguage and its DTO MLanguageDTO.
 */
@Mapper(componentModel = "spring", uses = {ResumeMapper.class})
public interface MLanguageMapper extends EntityMapper<MLanguageDTO, MLanguage> {

    @Mapping(source = "resume.id", target = "resumeId")
    @Mapping(source = "resume.name", target = "resumeName")
    MLanguageDTO toDto(MLanguage mLanguage); 

    @Mapping(source = "resumeId", target = "resume")
    MLanguage toEntity(MLanguageDTO mLanguageDTO);

    default MLanguage fromId(Long id) {
        if (id == null) {
            return null;
        }
        MLanguage mLanguage = new MLanguage();
        mLanguage.setId(id);
        return mLanguage;
    }
}
