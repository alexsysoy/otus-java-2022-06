package ru.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.client.domain.Message;

import static ru.client.controllers.MessageController.SECRET_ROOM;

@Service
public class DatastoreServiceImpl implements DatastoreService {
    private static final Logger log = LoggerFactory.getLogger(DatastoreServiceImpl.class);
    private final WebClient datastoreClient;

    public DatastoreServiceImpl(WebClient datastoreClient) {
        this.datastoreClient = datastoreClient;
    }

    @Override
    public Mono<Long> saveMessage(String roomId, Message message) {
        return datastoreClient.post().uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchangeToMono(response -> response.bodyToMono(Long.class));
    }

    @Override
    public Flux<Message> getMessagesByRoomId(long roomId) {
        return datastoreClient.get().uri(uriSelector(roomId))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Message.class);
                    } else {
                        return response.createException().flatMapMany(Mono::error);
                    }
                });
    }

    private String uriSelector(long roomId) {
        log.info("SELECTOR: {}", roomId);
        if (SECRET_ROOM != roomId) {
            return String.format("/msg/%s", roomId);
        }
        return "/allmsg";
    }
}
