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
}
