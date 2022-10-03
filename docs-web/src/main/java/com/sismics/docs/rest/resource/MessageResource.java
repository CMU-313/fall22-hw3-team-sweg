package com.sismics.docs.rest.resource;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import com.sismics.docs.core.dao.MessageDao;
import com.sismics.docs.core.listener.async.MessageAsyncListener;
import com.sismics.rest.exception.ForbiddenClientException;

/**
 * Message REST resources.
 */
@Path("/messages")
public class MessageResource extends BaseResource {
    /**
     * Long poll the number of unread messages until there is one.
     *
     * @api {get} /messages/unread_count Get the number of unread messages
     * @apiName GetUnreadMessagesCount
     * @apiGroup Messages
     * @apiSuccess {Integer} count The number of unread messages
     * @apiError (client) ForbiddenError Access denied
     * @apiPermission user
     * @apiVersion 1.5.0
     */
    @GET
    @Path("unread_count")
    public void getUnreadCount(@Suspended final AsyncResponse asyncResponse)
            throws InterruptedException {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        String userId = principal.getId();
        MessageDao messageDao = new MessageDao();
        int unreadCount = messageDao.getUnreadMsgCount(userId);
        if (unreadCount > 0) {
            JsonObject responseObj = Json.createObjectBuilder().add("count", unreadCount).build();
            asyncResponse.resume(Response.ok().entity(responseObj).build());
        } else if (MessageAsyncListener.isClientRegistered(userId)) {
            asyncResponse.cancel();
        } else {
            MessageAsyncListener.registerClient(userId, asyncResponse);
        }
    }
}
