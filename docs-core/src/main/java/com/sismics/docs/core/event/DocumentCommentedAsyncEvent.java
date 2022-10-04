package com.sismics.docs.core.event;

import com.google.common.base.MoreObjects;

/**
 * Document commented event.
 */
public class DocumentCommentedAsyncEvent extends UserEvent {
    /**
     * Document ID.
     */
    private String documentId;

    /**
     * Document owner ID.
     */
    private String ownerId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", getUserId())
                .add("documentId", documentId)
                .add("ownerId", ownerId)
                .toString();
    }
}
