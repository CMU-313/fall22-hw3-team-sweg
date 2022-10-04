package com.sismics.docs.core.dao;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sismics.docs.core.model.jpa.Message;
import com.sismics.util.context.ThreadLocalContext;

/**
 * Message DAO.
 */
public class MessageDao {
    /**
     * Returns a message by ID.
     * 
     * @param id Message ID
     * @return Message if exists, otherwise null
     */
    public Message getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        try {
            Query q = em.createQuery("select m from Message m where m.id = :id and m.deleteDate is null");
            q.setParameter("id", id);
            return (Message) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Creates a new message.
     * 
     * @param message Message
     * @return New message ID
     */
    public String create(Message message) {
        message.setId(UUID.randomUUID().toString());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(message);
        return message.getId();
    }

    /**
     * Extracts unread message counts.
     * 
     * @param userId User ID
     * @return Number of unread messages
     */
    public int getUnreadMsgCount(String userId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select count(m) from Message m where m.receiverId = :userId and m.isRead = False and m.deleteDate is null");
        q.setParameter("userId", userId);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Change status when message is read.
     * 
     * @param msgId Message ID
     * @param userId User Id
     * @return Existence of message
     */
    public boolean read(String msgId, String userId) {
        Message message = getById(msgId);
        if (message == null || message.getReceiverId() != userId) {
            return false;
        }
        message.setIsRead(true);
        return true;
    }
}