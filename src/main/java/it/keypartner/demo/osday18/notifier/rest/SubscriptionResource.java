package it.keypartner.demo.osday18.notifier.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SubscriptionResource {
	
	@POST
	@Path("project/{project-name}")
	Response subscripeToTaskNotification(@PathParam("project-name") String projectName, SubscriberInfo subscriberInfo);



}