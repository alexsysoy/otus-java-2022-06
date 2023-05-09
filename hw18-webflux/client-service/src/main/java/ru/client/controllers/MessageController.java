package ru.client.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.client.domain.Message;
import ru.client.service.ConnectionService;
import ru.client.service.DatastoreService;
import ru.client.util.Utils;

@Controller
public class MessageController {
    public static final long SECRET_ROOM = 1408;
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final DatastoreService datastoreService;
    private final ConnectionService connectionService;

    public MessageController(DatastoreService datastoreService, ConnectionService connectionService) {
        this.datastoreService = datastoreService;
        this.connectionService = connectionService;
    }

    @MessageMapping("/message.{roomId}")
    public void getMessage(@DestinationVariable String roomId, Message message) {
        log.info("get message:{}, roomId:{}", message, roomId);
        long id = Utils.parseRoomId(roomId);

        if (SECRET_ROOM == id) {
            log.info("message from room 1408");
        } else {
            datastoreService.saveMessage(roomId, message)
                    .subscribe(msgId -> log.info("message send id:{}", msgId));

            connectionService.sendMessageToFront(roomId, message.messageStr());
        }
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        log.info("session created");
        String simpDestination = connectionService.getSimpDestinationFromHeaders(event);

        long id = Utils.parseRoomId(simpDestination);

        datastoreService.getMessagesByRoomId(id)
                .doOnNext(message -> log.info("get another message: {}", message.messageStr()))
                .doOnError(ex -> log.error("getting messages for roomId:{} failed", id, ex))
                .subscribe(message -> connectionService.sendArchiveMessageToFront(simpDestination, message));
    }
}



