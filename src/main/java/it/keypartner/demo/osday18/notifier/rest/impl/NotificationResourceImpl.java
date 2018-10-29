package it.keypartner.demo.osday18.notifier.rest.impl;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.keypartner.demo.osday18.notifier.Constants;
import it.keypartner.demo.osday18.notifier.rest.NotificationResource;

public class NotificationResourceImpl implements NotificationResource {

    private static final Logger log = LoggerFactory.getLogger(NotificationResourceImpl.class);

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = Constants.QUEUE_BPM_NOTIFIER)
    private Queue bpmNotifierQueue;

    @Override
    public Response simulateNotification(String notificationPayload) {
        log.info("called simulateNotification with paylaod {}", notificationPayload);
        jmsContext.createProducer().send(bpmNotifierQueue, notificationPayload);
        return Response.ok().build();
    }
}
