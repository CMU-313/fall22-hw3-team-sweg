package com.sismics.docs.core.listener.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.sismics.docs.core.event.DocumentAssignedAsyncEvent;
import com.sismics.docs.core.util.TransactionUtil;

/**
 * Listener on document assigned.
 */
public class DocumentAssignedAsyncListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(DocumentAssignedAsyncListener.class);

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
            // TODO (Kyungmin): Handle document assigned event
            // 1. Create a message entity for the assignment
            // 2. Mark the message status of the assignee as dirty for the long polling
        });
    }
}
