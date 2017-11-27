package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.BasicInformationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BasicInformation and its DTO BasicInformationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BasicInformationMapper extends EntityMapper<BasicInformationDTO, BasicInformation> {

    

    @Mapping(target = "resume", ignore = true)
    BasicInformation toEntity(BasicInformationDTO basicInformationDTO);

    default BasicInformation fromId(Long id) {
        if (id == null) {
            return null;
        }
        BasicInformation basicInformation = new BasicInformation();
        basicInformation.setId(id);
        return basicInformation;
    }
}
