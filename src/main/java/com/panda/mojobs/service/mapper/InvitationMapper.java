package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.InvitationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Invitation and its DTO InvitationDTO.
 */
@Mapper(componentModel = "spring", uses = {SchoolMapper.class, UserMapper.class})
public interface InvitationMapper extends EntityMapper<InvitationDTO, Invitation> {

    @Mapping(source = "school.id", target = "schoolId")
    @Mapping(source = "school.name", target = "schoolName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    InvitationDTO toDto(Invitation invitation); 

    @Mapping(source = "schoolId", target = "school")
    @Mapping(source = "userId", target = "user")
    Invitation toEntity(InvitationDTO invitationDTO);

    default Invitation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Invitation invitation = new Invitation();
        invitation.setId(id);
        return invitation;
    }
}
