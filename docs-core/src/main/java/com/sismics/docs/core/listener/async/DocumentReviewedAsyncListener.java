package com.sismics.docs.core.listener.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.sismics.docs.core.event.DocumentReviewedAsyncEvent;
import com.sismics.docs.core.util.TransactionUtil;

/**
 * Listener on document reviewed.
 */
public class DocumentReviewedAsyncListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(DocumentReviewedAsyncListener.class);

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
            // TODO (Kyungmin): Handle document reviewed event
            // 1. Create a message entity for the assignment
            // 2. Mark the message status of the owner as dirty for the long polling
        });
    }
}
