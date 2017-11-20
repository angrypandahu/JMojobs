package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.TownDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Town and its DTO TownDTO.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class})
public interface TownMapper extends EntityMapper<TownDTO, Town> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    TownDTO toDto(Town town); 

    @Mapping(source = "cityId", target = "city")
    Town toEntity(TownDTO townDTO);

    default Town fromId(Long id) {
        if (id == null) {
            return null;
        }
        Town town = new Town();
        town.setId(id);
        return town;
    }
}
