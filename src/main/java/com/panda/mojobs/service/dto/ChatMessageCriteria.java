package com.panda.mojobs.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the ChatMessage entity. This class is used in ChatMessageResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /chat-messages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChatMessageCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LocalDateFilter sendTime;

    private StringFilter content;

    private LongFilter fromUserId;

    private LongFilter toUserId;

    public ChatMessageCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateFilter sendTime) {
        this.sendTime = sendTime;
    }

    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public LongFilter getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(LongFilter fromUserId) {
        this.fromUserId = fromUserId;
    }

    public LongFilter getToUserId() {
        return toUserId;
    }

    public void setToUserId(LongFilter toUserId) {
        this.toUserId = toUserId;
    }

    @Override
    public String toString() {
        return "ChatMessageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sendTime != null ? "sendTime=" + sendTime + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (fromUserId != null ? "fromUserId=" + fromUserId + ", " : "") +
                (toUserId != null ? "toUserId=" + toUserId + ", " : "") +
            "}";
    }

}
