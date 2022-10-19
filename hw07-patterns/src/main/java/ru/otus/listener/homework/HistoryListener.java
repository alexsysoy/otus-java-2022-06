package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.ArrayList;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final ArrayList<Message> HISTORY = new ArrayList<>();

    @Override
    public void onUpdated(Message msg) {
        HISTORY.add(new Message(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return HISTORY.stream().filter(message -> message.getId() == id).findFirst();
    }
}
