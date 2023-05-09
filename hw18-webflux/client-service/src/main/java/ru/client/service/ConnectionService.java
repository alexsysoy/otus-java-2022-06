package ru.client.service;

import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.client.domain.Message;

public interface ConnectionService {
    void sendMessageToFront(String roomId, String message);

    String getSimpDestinationFromHeaders(SessionSubscribeEvent event);

    void sendArchiveMessageToFront(String simpDestination, Message message);
}
