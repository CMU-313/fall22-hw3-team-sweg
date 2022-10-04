package com.sismics.docs.core.listener.async;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.sismics.docs.core.event.DocumentAssignedAsyncEvent;
import com.sismics.docs.core.event.DocumentCommentedAsyncEvent;
import com.sismics.docs.core.event.DocumentReviewedAsyncEvent;
import com.sismics.docs.core.util.TransactionUtil;
import com.sismics.docs.core.dao.MessageDao;
import com.sismics.docs.core.constant.MessageType;
import com.sismics.docs.core.model.jpa.Message;

/**
 * Listener on messages.
 */
public class MessageAsyncListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(MessageAsyncListener.class);

    /**
     * Client pool for MessageResource.
     */
    private static Map<String, AsyncResponse> clients = new ConcurrentHashMap<String, AsyncResponse>();

    /**
     * Document assigned.
     * 
     * @param event Document assigned event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void on(final DocumentAssignedAsyncEvent event) {
        if (log.isInfoEnabled()) {
            log.info("Document assigned event: " + event.toString());
        }

        TransactionUtil.handle(() -> {
            // TODO (Kyungmin): Create a message entity for the assignment
            Message message = new Message();
            message.setType(MessageType.DOCUMENT_ASSIGNED);
            message.setDocumentId(event.getDocumentId());
            message.setSenderId(event.getUserId());
            message.setReceiverId(event.getAssigneeId());
            MessageDao messageDao = new MessageDao();
            messageDao.create(message);

            sendUnreadCount(event.getAssigneeId());
        });
    }

    /**
     * Document commented.
     * 
     * @param event Document commented event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void on(final DocumentCommentedAsyncEvent event) {
        log.info("Document commented event: " + event.toString());

        TransactionUtil.handle(() -> {
            // TODO (Kyungmin): Create a message entity for the comment
            Message message = new Message();
            message.setType(MessageType.DOCUMENT_COMMENTED);
            message.setDocumentId(event.getDocumentId());
            message.setSenderId(event.getUserId());
            message.setReceiverId(event.getOwnerId());
            MessageDao messageDao = new MessageDao();
            messageDao.create(message);

            sendUnreadCount(event.getOwnerId());
        });
    }

    /**
     * Document reviewed.
     * 
     * @param event Document reviewed event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void on(final DocumentReviewedAsyncEvent event) {
        if (log.isInfoEnabled()) {
            log.info("Document reviewed event: " + event.toString());
        }

        TransactionUtil.handle(() -> {
            // TODO (Kyungmin): Create a message entity for the review
            Message message = new Message();
            message.setType(MessageType.DOCUMENT_REVIEWED);
            message.setDocumentId(event.getDocumentId());
            message.setSenderId(event.getUserId());
            message.setReceiverId(event.getOwnerId());
            MessageDao messageDao = new MessageDao();
            messageDao.create(message);

            sendUnreadCount(event.getOwnerId());
        });
    }

    /**
     * Returns whether the client is registered to the listener.  
     * 
     * @param userId User ID
     */
    public static boolean isClientRegistered(final String userId) {
        return clients.containsKey(userId);
    }

    /**
     * Registers an asynchronous client response object to the listener.
     * 
     * @param userId        User ID
     * @param asyncResponse JAX-RS asynchronous response object connected to the
     *                      user
     */
    public static void registerClient(final String userId, final AsyncResponse asyncResponse) {
        clients.put(userId, asyncResponse);
    }

    /**
     * Sends the number of unread messages to a client. This resolves the long
     * polling triggered by getUnreadCount.
     * 
     * @param userId User ID
     */
    private static void sendUnreadCount(final String userId) {
        AsyncResponse asyncResponse = clients.get(userId);
        if (asyncResponse == null) {
            return;
        }
        // TODO (Kyungmin): Fetch the number of unread messages
        int unreadCount = 1;
        if (unreadCount > 0) {
            JsonObject responseObj = Json.createObjectBuilder().add("count", unreadCount).build();
            asyncResponse.resume(Response.ok().entity(responseObj).build());
            clients.remove(userId);
        }
    }
}
