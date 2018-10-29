package it.keypartner.demo.osday18.notifier.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import it.keypartner.demo.osday18.notifier.rest.SubscriberInfo;

@ApplicationScoped
public class SubscriberStore {

    private Map<String, SubscriberInfo> subscriberInfos = new HashMap<>();
    public void add(SubscriberInfo subscriberInfo) {
        subscriberInfos.putIfAbsent(subscriberInfo.getChatId(), subscriberInfo);
    }

    public Collection<SubscriberInfo> getAll() {
        return subscriberInfos.values();
    }
}
