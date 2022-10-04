package com.sismics.docs.core.event;

import com.google.common.base.MoreObjects;

/**
 * Document assigned event.
 */
public class DocumentAssignedAsyncEvent extends UserEvent {
    /**
     * Document ID.
     */
    private String documentId;

    /**
     * Document assignee ID.
     */
    private String assigneeId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", getUserId())
                .add("documentId", documentId)
                .add("assigneeId", assigneeId)
                .toString();
    }
}
