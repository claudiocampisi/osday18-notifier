package it.keypartner.demo.osday18.notifier.rest.impl;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.ws.rs.core.Response;

import it.keypartner.demo.osday18.notifier.Constants;
import it.keypartner.demo.osday18.notifier.rest.SubscriberInfo;
import it.keypartner.demo.osday18.notifier.rest.SubscriptionResource;
import it.keypartner.demo.osday18.notifier.store.SubscriberStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscribtionResourceImpl implements SubscriptionResource {

    @Inject
    SubscriberStore subscriberStore;

    private static final Logger log = LoggerFactory.getLogger(SubscribtionResourceImpl.class);

    @Override
    public Response subscripeToTaskNotification(String projectName, SubscriberInfo subscriberInfo) {
    	log.info("Called subscripeToTaskNotification with projectname {}",projectName);
    	//context.createProducer().send(bpmNotifier, String.valueOf(subscriberInfo));
        //TODO Insert SubscriberInfo into @ApplicationContext Map
        subscriberStore.add(subscriberInfo);
        return Response.ok().build();
    }
}
