package com.sismics.docs.rest.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import com.sismics.docs.core.dao.MessageDao;
import com.sismics.docs.core.dao.criteria.MessageCriteria;
import com.sismics.docs.core.dao.dto.MessageDto;
import com.sismics.docs.core.listener.async.MessageAsyncListener;
import com.sismics.docs.core.util.jpa.SortCriteria;
import com.sismics.rest.exception.ForbiddenClientException;

/*
 * Message REST resources.
 */
@Path("/messages")
public class MessageResource extends BaseResource {
    /**
     * Returns all messages.
     *
     * @api {get} /messages Get messages
     * @apiName GetMessageList
     * @apiGroup Messages
     * @apiParam {Number} sort_column Column index to sort on
     * @apiParam {Boolean} asc If true, sort in ascending order
     * @apiParam {String} type Message type to filter by
     * @apiParam {Boolean} isRead If true, return only read messages
     * @apiSuccess {Object[]} messages List of messages
     * @apiSuccess {String} messages.id Message ID
     * @apiSuccess {String} messages.type Type
     * @apiSuccess {String} messages.sender Sender name
     * @apiSuccess {Boolean} messages.isRead Is read
     * @apiSuccess {String} messages.timestamp Timestamp
     * @apiError (client) ForbiddenError Access denied
     * @apiPermission user
     * @apiVersion 1.5.0
     *
     * @param sortColumn Sort index
     * @param asc        If true, ascending sorting, else descending
     * @param type       Message type to filter by
     * @param isRead     If true, return only read messages
     * @return Response
     */
    @GET
    public Response list(@QueryParam("sort") Integer sortColumn, @QueryParam("asc") Boolean asc,
            @QueryParam("type") String type, @QueryParam("isRead") Boolean isRead) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        JsonArrayBuilder messages = Json.createArrayBuilder();
        if (sortColumn == null) {
            // Sort by timestamp by default
            sortColumn = 4;
            asc = false;
        }
        SortCriteria sortCriteria = new SortCriteria(sortColumn, asc);
        MessageCriteria messageCriteria = new MessageCriteria(type, isRead);

        MessageDao messageDao = new MessageDao();
        List<MessageDto> messageDtoList = messageDao.findByCriteria(principal.getId(), messageCriteria, sortCriteria);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        for (MessageDto messageDto : messageDtoList) {
            messages.add(Json.createObjectBuilder()
                    .add("id", messageDto.getId())
                    .add("type", messageDto.getType().name())
                    .add("sender", messageDto.getSender())
                    .add("documentId",messageDto.getDocumentId())
                    .add("isRead", messageDto.getIsRead())
                    .add("timestamp", dateFormat.format(messageDto.getTimestamp())));
        }
        JsonObject responseObj = Json.createObjectBuilder().add("messages", messages).build();
        return Response.ok().entity(responseObj).build();
    }

    /**
     * Long poll the number of unread messages until there is one.
     *
     * @api {get} /messages/unread_count Get the number of unread messages
     * @apiName GetUnreadMessagesCount
     * @apiGroup Messages
     * @apiSuccess {Number} count The number of unread messages
     * @apiError (client) ForbiddenError Access denied
     * @apiPermission user
     * @apiVersion 1.5.0
     * 
     * @param count Current number of unread messages
     */
    @GET
    @Path("unread_count")
    public void getUnreadCount(@QueryParam("count") Integer count, @Suspended final AsyncResponse asyncResponse)
            throws InterruptedException {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        String userId = principal.getId();
        MessageDao messageDao = new MessageDao();
        int unreadCount = messageDao.getUnreadMsgCount(userId);
        if (unreadCount > 0 && unreadCount != count) {
            JsonObject responseObj = Json.createObjectBuilder().add("count", unreadCount).build();
            asyncResponse.resume(Response.ok().entity(responseObj).build());
        } else if (MessageAsyncListener.isClientRegistered(userId)) {
            asyncResponse.cancel();
        } else {
            MessageAsyncListener.registerClient(userId, asyncResponse);
        }
    }

    /**
     * Read a message.
     * 
     * @api {post} /messages/read Read a message.
     * @apiName PostMessageRead
     * @apiGroup Messages
     * @apiParam {String} id Message ID
     * @apiSuccess {String} status Status OK
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) NotFound Message not found
     * @apiPermission user
     * @apiVersion 1.5.0
     * 
     * @param msgId Message ID
     * @return Response
     */
    @POST
    @Path("read")
    public Response read(@FormParam("id") String msgId) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        String userId = principal.getId();
        MessageDao messageDao = new MessageDao();
        if (!messageDao.read(msgId, userId)) {
            throw new NotFoundException();
        }
        JsonObject responseObj = Json.createObjectBuilder().add("status", "ok").build();
        return Response.ok().entity(responseObj).build();
    }
}
