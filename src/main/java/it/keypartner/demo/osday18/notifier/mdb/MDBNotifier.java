package it.keypartner.demo.osday18.notifier.mdb;

import java.io.IOException;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import it.keypartner.demo.osday18.notifier.Constants;
import it.keypartner.demo.osday18.notifier.store.SubscriberStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@MessageDriven(name = "MDBNotifier", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = Constants.QUEUE_BPM_NOTIFIER),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class MDBNotifier implements MessageListener {

	private static final Logger log = LoggerFactory.getLogger(MDBNotifier.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	private ObjectReader reader = objectMapper.reader();

	private TypeReference<NotificationInfo> typeRef = new TypeReference<NotificationInfo>() {};
	
	@Inject
	SubscriberStore subscriberStore;

	@Override
	public void onMessage(Message message) {
		try{
			if(message instanceof TextMessage){
				String body = message.getBody(String.class);
				String telegramAccessTokern = System.getProperty(Constants.TELEGRAM_ACCESS_TOKEN_PROPERTY);
				log.info("Received Notification message -> " + body);
				JsonParser parser = objectMapper.getFactory().createParser(body);
				NotificationInfo notification = reader.readValue(parser,typeRef);
				String notificationText = "Notifica apertura task (" + notification.getTaskName() + ") per il deployment (" + notification.getDeploymentId() + ")";
				subscriberStore.getAll().forEach((subscriberInfo) -> {
					//TODO Send Notification via Telegram API
					log.info("Send Notification to chatId {}", subscriberInfo.getChatId());
					Response response = ClientBuilder.newClient().target("https://api.telegram.org/bot" + telegramAccessTokern + "/sendMessage")
							.queryParam("chat_id", subscriberInfo.getChatId())
							.queryParam("text",notificationText)
							.request().get();
				});

			}
		}catch (JMSException e) {
			throw new NotifierException("", e);
		}catch ( JsonParseException | JsonMappingException e)  {
			throw new NotifierException("", e);
		}catch (IOException e) {
			throw new NotifierException("", e);
		}
	}

	public static class NotifierException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NotifierException(String message) {
			super(message);
		}

		public NotifierException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
