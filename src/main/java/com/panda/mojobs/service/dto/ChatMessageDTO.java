package com.panda.mojobs.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ChatMessage entity.
 */
public class ChatMessageDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate sendTime;

    @NotNull
    @Size(max = 2000)
    private String content;

    private Long fromUserId;

    private String fromUserLogin;

    private Long toUserId;

    private String toUserLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDate sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long userId) {
        this.fromUserId = userId;
    }

    public String getFromUserLogin() {
        return fromUserLogin;
    }

    public void setFromUserLogin(String userLogin) {
        this.fromUserLogin = userLogin;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long userId) {
        this.toUserId = userId;
    }

    public String getToUserLogin() {
        return toUserLogin;
    }

    public void setToUserLogin(String userLogin) {
        this.toUserLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatMessageDTO chatMessageDTO = (ChatMessageDTO) o;
        if(chatMessageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatMessageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
            "id=" + getId() +
            ", sendTime='" + getSendTime() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
