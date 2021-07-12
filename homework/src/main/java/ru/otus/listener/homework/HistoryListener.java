package ru.otus.listener.homework;

import org.apache.commons.lang3.SerializationUtils;
import ru.otus.model.history.MessagesHistory;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.history.MessagesHistoryNotFoundException;

import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private final MessagesHistory messagesHistory;

    public HistoryListener(MessagesHistory messagesHistory) {
        this.messagesHistory = messagesHistory == null ? MessagesHistory.of() : messagesHistory;
    }

    @Override
    public void onUpdated(Message msg) {
        var msgCopy = SerializationUtils.clone(msg);
        messagesHistory.push(msgCopy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        try {
            var message = messagesHistory.getLast(id);
            return Optional.of(message);
        } catch (MessagesHistoryNotFoundException e) {
            return Optional.empty();
        }
    }
}
