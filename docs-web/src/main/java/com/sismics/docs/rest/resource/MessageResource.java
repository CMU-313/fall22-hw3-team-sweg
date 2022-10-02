package com.sismics.docs.rest.resource;

import java.util.concurrent.TimeUnit;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;

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
    }
}
