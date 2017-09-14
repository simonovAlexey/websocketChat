/**
 * ChatClientEndpoint.java
 * http://programmingforliving.com
 */
package org.simonov.websocketBot;

import javax.websocket.*;
import java.net.URI;

/**
 * ChatServer Client
 */
@ClientEndpoint
public class ChatClientEndpoint {
    Session userSession = null;
    private MessageHandler messageHandler;

    public ChatClientEndpoint(URI endpointURI) {
        try {
            System.out.println("Trying to connect...");
            WebSocketContainer container = ContainerProvider
                    .getWebSocketContainer();
            container.connectToServer(this, endpointURI);
            System.out.println("Connected to "+ endpointURI);
        } catch (Exception e) {
            System.out.println("Throw RTE:");
            throw new RuntimeException(e);
        }
    }


    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("Open session # : " + userSession.getId());
        this.userSession = userSession;
    }


    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("Close session # : " + userSession.getId()+ " reason: " + reason.toString());
        this.userSession = null;
    }


    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message Received: " + message);
        if (this.messageHandler != null)
            this.messageHandler.handleMessage(message);
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        System.out.println(thr.getLocalizedMessage());
    }


    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }


    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }


    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}
