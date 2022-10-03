package com.sismics.docs.core.dao.jpa;

import org.junit.Assert;
import org.junit.Test;

import com.sismics.docs.BaseTransactionalTest;
import com.sismics.docs.core.constant.MessageType;
import com.sismics.docs.core.dao.MessageDao;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.model.jpa.Message;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.util.TransactionUtil;

/**
 * Tests the message JPA.
 */
public class TestMessage extends BaseTransactionalTest {
    /**
     * Tests the message JPA.
     */
    @Test
    public void testMessage() throws Exception {
        // Create a sender
        UserDao senderDao = new UserDao();
        User sender = new User();
        sender.setUsername("sender");
        sender.setPassword("12345678");
        sender.setEmail("sender@docs.com");
        sender.setRoleId("admin");
        sender.setStorageQuota(10L);
        String senderId = senderDao.create(sender, "me");

        // Create a receiver
        UserDao receiverDao = new UserDao();
        User receiver = new User();
        receiver.setUsername("receiver");
        receiver.setPassword("12345678");
        receiver.setEmail("receiver@docs.com");
        receiver.setRoleId("admin");
        receiver.setStorageQuota(10L);
        String receiverId = receiverDao.create(receiver, "me");

        // Create a message
        MessageDao messageDao = new MessageDao();
        Message message = new Message();
        message.setType(MessageType.DOCUMENT_COMMENTED);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        String messageId = messageDao.create(message);

        TransactionUtil.commit();

        // Search a comment by ID
        message = messageDao.getById(messageId);
        Assert.assertNotNull(message);
        Assert.assertEquals(MessageType.DOCUMENT_COMMENTED, message.getType());
        Assert.assertEquals(senderId, message.getSenderId());
        Assert.assertEquals(receiverId, message.getReceiverId());
        Assert.assertFalse(message.getIsRead());
    }
}
