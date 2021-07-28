package ru.otus.model.history;

import ru.otus.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MessagesHistory class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class MessagesHistory {
    private final List<Message> messages;

    private MessagesHistory(List<Message> messages) {
        this.messages = messages == null ? new ArrayList<>() : messages;
    }

    public static MessagesHistory of() {
        return new MessagesHistory(new ArrayList<>());
    }

    public void push(Message message) {
        messages.add(message);
    }

    public Optional<Message> get(long messageId) {
        return messages.stream()
            .filter(m -> m.getId() == messageId)
            .findFirst();
    }
}
