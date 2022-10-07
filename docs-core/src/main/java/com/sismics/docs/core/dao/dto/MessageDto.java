package com.sismics.docs.core.dao.dto;

import java.util.Date;

import com.sismics.docs.core.constant.MessageType;

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
    private MessageType type;

    /**
     * Sender name.
     */
    private String sender;

    /**
     * Document ID.
     */
    private String documentId;

    /**
     * Document title. 
     */
    private String documentTitle;

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

    public MessageType getType() {
        return type;
    }

    public MessageDto setType(final MessageType type) {
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

    public String getDocumentId() {
        return documentId;
    }

    public MessageDto setDocumentId(final String documentId) {
        this.documentId = documentId;
        return this;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public MessageDto setDocumentTitle(final String documentTitle) {
        this.documentTitle = documentTitle;
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
