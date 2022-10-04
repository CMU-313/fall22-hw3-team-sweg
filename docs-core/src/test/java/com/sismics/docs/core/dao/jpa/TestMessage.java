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

        // Create 10 messages
        MessageDao messageDao = new MessageDao();
        String[] messageIds = new String[10];
        for (int i = 0; i < 10; i++) {
            boolean isRead = i % 2 == 0;
            Message message = new Message();
            message.setType(MessageType.DOCUMENT_COMMENTED);
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);
            message.setIsRead(isRead);
            messageIds[i] = messageDao.create(message);
        }

        TransactionUtil.commit();

        // Search a message by ID
        Message message = messageDao.getById(messageIds[0]);
        Assert.assertNotNull(message);
        Assert.assertEquals(MessageType.DOCUMENT_COMMENTED, message.getType());
        Assert.assertEquals(senderId, message.getSenderId());
        Assert.assertEquals(receiverId, message.getReceiverId());
        Assert.assertTrue(message.getIsRead());

        // Test unread message count
        Assert.assertEquals(5, messageDao.getUnreadMsgCount(receiverId));
        for (int i = 0; i < 10; i++) {
            if (i % 2 != 0) {
                messageDao.read(messageIds[i],receiverId);
            }
        }
        Assert.assertEquals(0, messageDao.getUnreadMsgCount(receiverId));
    }
}
