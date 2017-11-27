package com.panda.mojobs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ChatMessage.
 */
@Entity
@Table(name = "chat_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "chatmessage")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "send_time", nullable = false)
    private LocalDate sendTime;

    @NotNull
    @Size(max = 2000)
    @Column(name = "content", length = 2000, nullable = false)
    private String content;

    @ManyToOne(optional = false)
    @NotNull
    private User fromUser;

    @ManyToOne(optional = false)
    @NotNull
    private User toUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSendTime() {
        return sendTime;
    }

    public ChatMessage sendTime(LocalDate sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public void setSendTime(LocalDate sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public ChatMessage content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getFromUser() {
        return fromUser;
    }

    public ChatMessage fromUser(User user) {
        this.fromUser = user;
        return this;
    }

    public void setFromUser(User user) {
        this.fromUser = user;
    }

    public User getToUser() {
        return toUser;
    }

    public ChatMessage toUser(User user) {
        this.toUser = user;
        return this;
    }

    public void setToUser(User user) {
        this.toUser = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatMessage chatMessage = (ChatMessage) o;
        if (chatMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
            "id=" + getId() +
            ", sendTime='" + getSendTime() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
