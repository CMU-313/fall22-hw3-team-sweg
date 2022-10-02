package com.sismics.docs.core.listener.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.sismics.docs.core.event.DocumentCommentedAsyncEvent;
import com.sismics.docs.core.util.TransactionUtil;

/**
 * Listener on document commented.
 */
public class DocumentCommentedAsyncListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(DocumentCommentedAsyncListener.class);

    /**
     * Document commented.
     * 
     * @param event Document commented event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void on(final DocumentCommentedAsyncEvent event) {
        if (log.isInfoEnabled()) {
            log.info("Document commented event: " + event.toString());
        }

        TransactionUtil.handle(() -> {
            // TODO (Kyungmin): Handle document commented event
            // 1. Create a message entity for the assignment
            // 2. Mark the message status of the owner as dirty for the long polling
        });
    }
}
