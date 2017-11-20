package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.SchoolDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity School and its DTO SchoolDTO.
 */
@Mapper(componentModel = "spring", uses = {MojobImageMapper.class})
public interface SchoolMapper extends EntityMapper<SchoolDTO, School> {

    @Mapping(source = "logo.id", target = "logoId")
    @Mapping(source = "logo.name", target = "logoName")
    SchoolDTO toDto(School school); 

    @Mapping(source = "logoId", target = "logo")
    @Mapping(target = "jobs", ignore = true)
    School toEntity(SchoolDTO schoolDTO);

    default School fromId(Long id) {
        if (id == null) {
            return null;
        }
        School school = new School();
        school.setId(id);
        return school;
    }
}
