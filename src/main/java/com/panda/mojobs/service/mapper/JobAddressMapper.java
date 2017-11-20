package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.JobAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity JobAddress and its DTO JobAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, MjobMapper.class})
public interface JobAddressMapper extends EntityMapper<JobAddressDTO, JobAddress> {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "address.name", target = "addressName")
    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "job.name", target = "jobName")
    JobAddressDTO toDto(JobAddress jobAddress); 

    @Mapping(source = "addressId", target = "address")
    @Mapping(source = "jobId", target = "job")
    JobAddress toEntity(JobAddressDTO jobAddressDTO);

    default JobAddress fromId(Long id) {
        if (id == null) {
            return null;
        }
        JobAddress jobAddress = new JobAddress();
        jobAddress.setId(id);
        return jobAddress;
    }
}
