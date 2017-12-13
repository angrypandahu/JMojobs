package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.BasicInformationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BasicInformation and its DTO BasicInformationDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface BasicInformationMapper extends EntityMapper<BasicInformationDTO, BasicInformation> {

    @Mapping(source = "image.id", target = "imageId")
    BasicInformationDTO toDto(BasicInformation basicInformation); 

    @Mapping(target = "resume", ignore = true)
    @Mapping(source = "imageId", target = "image")
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
