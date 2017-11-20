package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.MjobDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Mjob and its DTO MjobDTO.
 */
@Mapper(componentModel = "spring", uses = {SchoolMapper.class, JobSubTypeMapper.class})
public interface MjobMapper extends EntityMapper<MjobDTO, Mjob> {

    @Mapping(source = "school.id", target = "schoolId")
    @Mapping(source = "school.name", target = "schoolName")
    @Mapping(source = "subType.id", target = "subTypeId")
    @Mapping(source = "subType.name", target = "subTypeName")
    MjobDTO toDto(Mjob mjob); 

    @Mapping(source = "schoolId", target = "school")
    @Mapping(source = "subTypeId", target = "subType")
    Mjob toEntity(MjobDTO mjobDTO);

    default Mjob fromId(Long id) {
        if (id == null) {
            return null;
        }
        Mjob mjob = new Mjob();
        mjob.setId(id);
        return mjob;
    }
}
