package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.SchoolDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity School and its DTO SchoolDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface SchoolMapper extends EntityMapper<SchoolDTO, School> {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "address.name", target = "addressName")
    SchoolDTO toDto(School school); 

    @Mapping(source = "addressId", target = "address")
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
