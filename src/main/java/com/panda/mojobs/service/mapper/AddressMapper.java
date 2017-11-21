package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.AddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Address and its DTO AddressDTO.
 */
@Mapper(componentModel = "spring", uses = {ProvinceMapper.class, CityMapper.class, TownMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "province.name", target = "provinceName")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "town.id", target = "townId")
    @Mapping(source = "town.name", target = "townName")
    AddressDTO toDto(Address address); 

    @Mapping(source = "provinceId", target = "province")
    @Mapping(source = "cityId", target = "city")
    @Mapping(source = "townId", target = "town")
    Address toEntity(AddressDTO addressDTO);

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
