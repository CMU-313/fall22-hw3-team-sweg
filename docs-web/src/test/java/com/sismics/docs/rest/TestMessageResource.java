package com.sismics.docs.rest;

import java.util.Date;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

import org.junit.Assert;
import org.junit.Test;

import com.sismics.util.filter.TokenBasedSecurityFilter;

/**
 * Test the message resource.
 */
public class TestMessageResource extends BaseJerseyTest {
    /**
     * Test the message resource.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testMessageResource() throws InterruptedException {
        // Login admin
        String adminToken = clientUtil.login("admin", "admin", false);

        // Login document1
        clientUtil.createUser("document1");
        String document1Token = clientUtil.login("document1");

        // Create a document with document1
        long create1Date = new Date().getTime();
        JsonObject json = target().path("/document").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, document1Token)
                .put(Entity.form(new Form()
                        .param("title", "My super title document 1")
                        .param("description", "My super description for document 1")
                        .param("language", "eng")
                        .param("create_date", Long.toString(create1Date))), JsonObject.class);
        String document1Id = json.getString("id");
        Assert.assertNotNull(document1Id);

        // Create a comment with admin
        Thread adminThread = new Thread(new AdminRunnable(target(), adminToken, document1Id));
        adminThread.start();

        // Get the number of unread messages for document1
        json = target().path("/messages/unread_count").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, document1Token)
                .get(JsonObject.class);
        Assert.assertEquals(1, json.getInt("count"));

        // Get the list of messages without any criteria
        json = target().path("/messages").request().cookie(TokenBasedSecurityFilter.COOKIE_NAME, document1Token)
                .get(JsonObject.class);
        JsonArray messages = json.getJsonArray("messages");
        Assert.assertEquals(1, messages.size());
        String messageId = messages.getJsonObject(0).getString("id");

        // Get the list of read messages
        json = target().path("/messages")
                .queryParam("isRead", true)
                .request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, document1Token)
                .get(JsonObject.class);
        messages = json.getJsonArray("messages");
        Assert.assertEquals(0, messages.size());

        // Get the list of DOCUMENT_COMMENTED messages
        json = target().path("/messages")
                .queryParam("type", "DOCUMENT_COMMENTED")
                .request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, document1Token)
                .get(JsonObject.class);
        messages = json.getJsonArray("messages");
        Assert.assertEquals(1, messages.size());

        // Read the unread message for document1
        json = target().path("/messages/read")
                .request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, document1Token)
                .post(Entity.form(new Form().param("id", messageId)), JsonObject.class);
        Assert.assertEquals("ok", json.getString("status"));

        // Get the list of read messages again
        json = target().path("/messages")
                .queryParam("isRead", true)
                .request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, document1Token)
                .get(JsonObject.class);
        messages = json.getJsonArray("messages");
        Assert.assertEquals(1, messages.size());
    }
}

class AdminRunnable implements Runnable {
    private WebTarget target;
    private String token;
    private String documentId;

    public AdminRunnable(final WebTarget target, final String token, final String documentId) {
        this.target = target;
        this.token = token;
        this.documentId = documentId;
    }

    public void run() {
        try {
            Thread.sleep(3000);
            target.path("/comment").request()
                    .cookie(TokenBasedSecurityFilter.COOKIE_NAME, token)
                    .put(Entity.form(new Form()
                            .param("id", documentId)
                            .param("content", "Comment by admin")), JsonObject.class);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
