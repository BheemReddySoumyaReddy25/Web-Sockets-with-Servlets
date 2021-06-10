package com.websockets;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import javax.json.*;
import java.io.StringWriter;
@ServerEndpoint(value = "/VitalCheckEndPoint",configurator = VitalCheckConfigurator.class)
public class VitalCheckEndPoint {
	static Set<Session> subscribers = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
    public void handleOpen(EndpointConfig endpointconfig,Session userSession) {
		userSession.getUserProperties().put("username", endpointconfig.getUserProperties().get("username"));
		subscribers.add(userSession);
	}
	@OnMessage
	public void handleMessage(String message,Session userSession) {
	      String username =(String) userSession.getUserProperties().get("username");
	      if(username!=null && !username.equals("doctor")) {
				subscribers.stream().forEach( x -> {
						try {
							if(x.getUserProperties().get("username").equals("doctor")) {
								x.getBasicRemote().sendText(buildJSON(username,message));
							}
						}
						catch(Exception e){
							e.getStackTrace();
						}
				});
			}
			else if(username!=null && username.equals("doctor")) {
				String[] messages = message.split(",");
				String patient  = messages[0];
				String subject = messages[1];
				subscribers.stream().forEach(x->{
					try {
						if(subject.equals("ambulance")) {
							if(x.getUserProperties().get("username").equals(patient)) {
								x.getBasicRemote().sendText(buildJSON("doctor","has summoned an ambulance"));
							}
							else if(x.getUserProperties().get("username").equals("ambulance")) {
								x.getBasicRemote().sendText(buildJSON(patient,"Requires an ambulance,"+messages[2]));
							}
						}
						else if(subject.equals("medication")) {
							if(x.getUserProperties().get("username").equals(patient)) {
								x.getBasicRemote().sendText(buildJSON("doctor",messages[2]+","+messages[3]));
							}
							
						}
					}
					catch(Exception e){
						e.getStackTrace();
					}
				});
			}
	}
	@OnClose
    public void handleClose(Session userSession){
		subscribers.remove(userSession);
	}
	
	@OnError
    public void handleError(Throwable t){
		
	}
	private String buildJSON(String username,String message)
	{
		 // JsonObjectBuilder -  initializes an empty JSON object model and provides methods to add name/value pairs to the object model and to return the resulting object
		JsonObject jsonObject=Json.createObjectBuilder().add("message",username+","+message).build();
	
		StringWriter stringWriter=new StringWriter();
	
		// createWriter()-Creates a JSON writer to write a JSON object or array structure to the specified character stream.
	
		try(JsonWriter jsonWriter=Json.createWriter(stringWriter))
		{
		      jsonWriter.write(jsonObject);
		 }
		 return    stringWriter.toString();
	}
    
}
