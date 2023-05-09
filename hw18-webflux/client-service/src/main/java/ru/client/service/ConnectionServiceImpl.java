package ru.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import ru.client.controllers.ChatException;
import ru.client.domain.Message;
import ru.client.util.Utils;

import static ru.client.controllers.MessageController.SECRET_ROOM;
import static ru.client.util.Utils.TOPIC_TEMPLATE;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private static final Logger log = LoggerFactory.getLogger(ConnectionServiceImpl.class);
    private final SimpMessagingTemplate template;

    public ConnectionServiceImpl(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void sendMessageToFront(String roomId, String message) {
        if (SECRET_ROOM != Utils.parseRoomId(roomId)) {
            template.convertAndSend(String.format("%s%s", TOPIC_TEMPLATE, roomId), new Message(HtmlUtils.htmlEscape(message)));
        }
        template.convertAndSend(String.format("%s%s", TOPIC_TEMPLATE, SECRET_ROOM), new Message(HtmlUtils.htmlEscape(message)));
    }

    @Override
    public String getSimpDestinationFromHeaders(SessionSubscribeEvent event) {
        var genericMessage = (GenericMessage<byte[]>) event.getMessage();

        var simpDestination = (String) genericMessage.getHeaders().get("simpDestination");
        if (simpDestination == null) {
            log.error("can not get simpDestination header, headers:{}", genericMessage.getHeaders());
            throw new ChatException("Can not get simpDestination header");
        }
        return simpDestination;
    }

    @Override
    public void sendArchiveMessageToFront(String simpDestination, Message message) {
        template.convertAndSend(simpDestination, message);
    }
}
