package ru.otus.listener.homework;

import ru.otus.model.history.MessagesHistory;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private final MessagesHistory messagesHistory;

    public HistoryListener(MessagesHistory messagesHistory) {
        this.messagesHistory = messagesHistory == null ? MessagesHistory.of() : messagesHistory;
    }

    @Override
    public void onUpdated(Message msg) {
        var msgCopy = msg.clone();
        messagesHistory.push(msgCopy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return messagesHistory.get(id);
    }
}
