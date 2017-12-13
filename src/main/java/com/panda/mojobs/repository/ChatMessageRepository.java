package com.panda.mojobs.repository;

import com.panda.mojobs.domain.ChatMessage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the ChatMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, JpaSpecificationExecutor<ChatMessage> {

    @Query("select chat_message from ChatMessage chat_message where chat_message.fromUser.login = ?#{principal.username}")
    List<ChatMessage> findByFromUserIsCurrentUser();

    @Query("select chat_message from ChatMessage chat_message where chat_message.toUser.login = ?#{principal.username}")
    List<ChatMessage> findByToUserIsCurrentUser();

}
