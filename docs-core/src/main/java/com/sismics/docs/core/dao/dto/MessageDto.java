package com.sismics.docs.core.dao.dto;

import java.util.Date;

/**
 * Message DTO.
 */
public class MessageDto {
    /**
     * Message ID.
     */
    private String id;

    /**
     * Type.
     */
    private String type;

    /**
     * Sender name.
     */
    private String sender;

    /**
     * Is read.
     */
    private boolean isRead;

    /**
     * Timestamp.
     */
    private Date timestamp;

    public String getId() {
        return id;
    }

    public MessageDto setId(final String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public MessageDto setType(final String type) {
        this.type = type;
        return this;
    }

    public String getSender() {
        return sender;
    }

    public MessageDto setSender(final String sender) {
        this.sender = sender;
        return this;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public MessageDto setIsRead(final boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public MessageDto setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
