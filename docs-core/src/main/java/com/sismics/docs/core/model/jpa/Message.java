package com.sismics.docs.core.model.jpa;

import java.util.Date;

import javax.persistence.*;

import com.google.common.base.MoreObjects;
import com.sismics.docs.core.constant.MessageType;

/**
 * Message entity.
 */
@Entity
@Table(name = "T_MESSAGES")
public class Message implements Loggable {
    /**
     * Message ID.
     */
    @Id
    @Column(name = "MSG_ID_C", nullable = false, length = 36)
    private String id;

    /**
     * Type.
     */
    @Column(name = "MSG_TYPE_C", nullable = false, length = 40)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    /**
     * Document ID.
     */
    @Column(name = "MSG_IDDOCUMENT_C", length = 36)
    private String documentId;

    /**
     * Sender user ID.
     */
    @Column(name = "MSG_IDSENDER_C", nullable = false, length = 36)
    private String senderId;

    /**
     * Receiver user ID.
     */
    @Column(name = "MSG_IDRECEIVER_C", nullable = false, length = 36)
    private String receiverId;

    /**
     * Is read.
     */
    @Column(name = "MSG_ISREAD_B", nullable = false)
    private boolean isRead = false;

    /**
     * Creation time.
     */
    @Column(name = "MSG_CREATETIME_D", nullable = false)
    private Date createDate = new Date();

    /**
     * Deletion time.
     */
    @Column(name = "MSG_DELETETIME_D")
    private Date deleteDate;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(final MessageType type) {
        this.type = type;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final String documentId) {
        this.documentId = documentId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(final String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(final String receiverId) {
        this.receiverId = receiverId;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(final boolean isRead) {
        this.isRead = isRead;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(final Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public String toMessage() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type.name())
                .toString();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type.name())
                .add("documentId", documentId)
                .add("senderId", senderId)
                .add("receiverId", receiverId)
                .add("isRead", isRead)
                .toString();
    }
}
