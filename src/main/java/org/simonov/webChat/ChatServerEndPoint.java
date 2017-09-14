package org.simonov.webChat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ChatServer
 */
@ServerEndpoint(value = "/chat")
@Singleton
public class ChatServerEndPoint {
    final static Set<Session> userSessions = Collections.synchronizedSet(new HashSet<Session>());
    private static final Logger LOG = LoggerFactory.getLogger(ChatServerEndPoint.class);

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("New request received. Id: " + userSession.getId());
        userSessions.add(userSession);
    }

    @OnClose
    public void onClose(Session userSession) {
        System.out.println("Connection closed. Id: " + userSession.getId());
        userSessions.remove(userSession);
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        LOG.info("Message Received: " + message + " from: " + userSession.getId() );

        for (Session session : userSessions) {
            LOG.debug("Send message to session #: {}",session.getId());
            if (session.isOpen())
                session.getAsyncRemote().sendText(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        LOG.info("onError in session {} details: {}",session.getId(),thr.toString());
    }
}
