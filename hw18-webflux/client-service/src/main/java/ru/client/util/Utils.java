package ru.client.util;

import ru.client.controllers.ChatException;

public class Utils {

    private Utils() {
    }

    public static final String TOPIC_TEMPLATE = "/topic/response.";
    public static long parseRoomId(String simpDestination) {
        try {
            return Long.parseLong(simpDestination.replace(TOPIC_TEMPLATE, ""));
        } catch (Exception ex) {
            throw new ChatException("Can not get roomId");
        }
    }
}
