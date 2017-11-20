package com.panda.mojobs.service.mapper;

import com.panda.mojobs.domain.*;
import com.panda.mojobs.service.dto.ChatMessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ChatMessage and its DTO ChatMessageDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChatMessageMapper extends EntityMapper<ChatMessageDTO, ChatMessage> {

    @Mapping(source = "fromUser.id", target = "fromUserId")
    @Mapping(source = "fromUser.login", target = "fromUserLogin")
    @Mapping(source = "toUser.id", target = "toUserId")
    @Mapping(source = "toUser.login", target = "toUserLogin")
    ChatMessageDTO toDto(ChatMessage chatMessage); 

    @Mapping(source = "fromUserId", target = "fromUser")
    @Mapping(source = "toUserId", target = "toUser")
    ChatMessage toEntity(ChatMessageDTO chatMessageDTO);

    default ChatMessage fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(id);
        return chatMessage;
    }
}
