package ru.client.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.client.domain.Message;

public interface DatastoreService {
    Mono<Long> saveMessage(String roomId, Message message);
    Flux<Message> getMessagesByRoomId(long roomId);
}
