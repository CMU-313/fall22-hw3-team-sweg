package com.sismics.docs.core.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.google.common.base.Joiner;
import com.sismics.docs.core.constant.MessageType;
import com.sismics.docs.core.dao.criteria.MessageCriteria;
import com.sismics.docs.core.dao.dto.MessageDto;
import com.sismics.docs.core.model.jpa.Message;
import com.sismics.docs.core.util.jpa.QueryParam;
import com.sismics.docs.core.util.jpa.QueryUtil;
import com.sismics.docs.core.util.jpa.SortCriteria;
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
        Query q = em.createQuery(
                "select count(m) from Message m where m.receiverId = :userId and m.isRead = False and m.deleteDate is null");
        q.setParameter("userId", userId);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Change status when message is read.
     * 
     * @param msgId  Message ID
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

    /**
     * Returns the list of all messages.
     * 
     * @param criteria     Search criteria
     * @param sortCriteria Sort criteria
     * @return List of all the messages for a user.
     */
    public List<MessageDto> findByCriteria(String userId, MessageCriteria criteria, SortCriteria sortCriteria) {
        List<String> criteriaList = new ArrayList<String>();
        Map<String, Object> parameterMap = new HashMap<String, Object>();

        StringBuilder sb = new StringBuilder(
                "select m.MSG_ID_C as c0, m.MSG_TYPE_C as c1, u.USE_USERNAME_C as c2, m.MSG_ISREAD_B as c3, m.MSG_CREATETIME_D as c4 from T_MESSAGES m join T_USER u on u.USE_ID_C = m.MSG_IDSENDER_C");
        criteriaList.add("m.MSG_IDRECEIVER_C = :userId");
        parameterMap.put("userId", userId);

        Boolean isRead = criteria.getIsRead();
        if (isRead != null) {
            criteriaList.add("m.MSG_ISREAD_B = :isRead");
            parameterMap.put("isRead", isRead);
        }

        MessageType msgType = criteria.getType();
        if (msgType != null) {
            criteriaList.add("m.MSG_TYPE_C = :msgType");
            parameterMap.put("msgType", msgType.name());
        }

        sb.append(" where ");
        sb.append(Joiner.on(" and ").join(criteriaList));

        QueryParam queryParam = QueryUtil.getSortedQueryParam(new QueryParam(sb.toString(), parameterMap),
                sortCriteria);
        @SuppressWarnings("unchecked")
        List<Object[]> l = QueryUtil.getNativeQuery(queryParam).getResultList();

        List<MessageDto> messageDtoList = new ArrayList<>();
        for (Object[] o : l) {
            int i = 0;
            MessageDto messageDto = new MessageDto()
                    .setId((String) o[i++])
                    .setType(MessageType.valueOf((String) o[i++]))
                    .setSender((String) o[i++])
                    .setDocumentId((String) o[i++])
                    .setIsRead((boolean) o[i++])
                    .setTimestamp((Date) o[i++]);
            messageDtoList.add(messageDto);
        }
        return messageDtoList;
    }
}
