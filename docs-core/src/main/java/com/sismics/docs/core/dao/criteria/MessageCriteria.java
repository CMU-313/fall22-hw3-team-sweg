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
    private boolean isRead;

    public MessageType getType() {
        return type;
    }

    public void setType(final MessageType type) {
        this.type = type;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(final boolean isRead) {
        this.isRead = isRead;
    }
}
