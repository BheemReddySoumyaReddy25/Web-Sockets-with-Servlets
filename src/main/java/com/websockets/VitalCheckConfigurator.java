package com.websockets;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.*;

public class VitalCheckConfigurator extends ServerEndpointConfig.Configurator{
	public void modifyHandshake(ServerEndpointConfig sec,HandshakeRequest request,HandshakeResponse response){
		sec.getUserProperties().put("username",(String)((HttpSession)request.getHttpSession()).getAttribute("username"));
	}
}
