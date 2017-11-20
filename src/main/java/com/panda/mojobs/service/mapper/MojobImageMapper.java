package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.MojobImageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MojobImage and its DTO MojobImageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MojobImageMapper extends EntityMapper<MojobImageDTO, MojobImage> {

    

    

    default MojobImage fromId(Long id) {
        if (id == null) {
            return null;
        }
        MojobImage mojobImage = new MojobImage();
        mojobImage.setId(id);
        return mojobImage;
    }
}
