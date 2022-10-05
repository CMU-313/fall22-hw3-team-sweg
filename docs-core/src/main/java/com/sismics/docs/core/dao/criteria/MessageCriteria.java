package com.sismics.docs.core.dao.criteria;

import com.sismics.docs.core.constant.MessageType;

/**
 * Message criteria.
 */
public class MessageCriteria {
    /**
     * Type.
     */
    private MessageType type;

    /**
     * Is read.
     */
    private Boolean isRead;

    /**
     * Constructor of MessageCriteria.
     */
    public MessageCriteria(String type, Boolean isRead) {
        if (type != null) {
            this.type = MessageType.valueOf(type);
        } else {
            this.type = null;
        }
        this.isRead = isRead;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(final MessageType type) {
        this.type = type;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(final Boolean isRead) {
        this.isRead = isRead;
    }
}
