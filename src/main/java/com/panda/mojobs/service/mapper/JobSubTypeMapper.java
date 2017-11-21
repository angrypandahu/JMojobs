package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.JobSubTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity JobSubType and its DTO JobSubTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobSubTypeMapper extends EntityMapper<JobSubTypeDTO, JobSubType> {

    

    

    default JobSubType fromId(Long id) {
        if (id == null) {
            return null;
        }
        JobSubType jobSubType = new JobSubType();
        jobSubType.setId(id);
        return jobSubType;
    }
}
