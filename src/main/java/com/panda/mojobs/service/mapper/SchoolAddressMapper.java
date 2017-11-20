package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.SchoolAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SchoolAddress and its DTO SchoolAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, SchoolMapper.class})
public interface SchoolAddressMapper extends EntityMapper<SchoolAddressDTO, SchoolAddress> {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "address.name", target = "addressName")
    @Mapping(source = "school.id", target = "schoolId")
    @Mapping(source = "school.name", target = "schoolName")
    SchoolAddressDTO toDto(SchoolAddress schoolAddress); 

    @Mapping(source = "addressId", target = "address")
    @Mapping(source = "schoolId", target = "school")
    SchoolAddress toEntity(SchoolAddressDTO schoolAddressDTO);

    default SchoolAddress fromId(Long id) {
        if (id == null) {
            return null;
        }
        SchoolAddress schoolAddress = new SchoolAddress();
        schoolAddress.setId(id);
        return schoolAddress;
    }
}
