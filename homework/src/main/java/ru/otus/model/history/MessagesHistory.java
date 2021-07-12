package ru.otus.model.history;

import ru.otus.model.Message;

import java.util.*;

/**
 * MessagesHistory class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class MessagesHistory {
    private final Map<Long, Deque<Message>> messagesMap;

    private MessagesHistory(Map<Long, Deque<Message>> messagesMap) {
        this.messagesMap = messagesMap == null ? new HashMap<>() : messagesMap;
    }

    public static MessagesHistory of() {
        return new MessagesHistory(null);
    }

    public void push(Message message) {
        Long messageId = message.getId();

        var messages = messagesMap.computeIfAbsent(messageId, k -> new ArrayDeque<>());
        messages.push(message);
    }

    public Message getLast(Long messageId) throws MessagesHistoryEmptyException {
        var messages = messagesMap.get(messageId);
        if (messages == null) {
            var errorMessage = String.format("Messages history for %d message not found", messageId);
            throw new MessagesHistoryNotFoundException(errorMessage);
        }

        try {
            return messages.getLast();
        } catch (NoSuchElementException e) {
            throw new MessagesHistoryEmptyException("Messages history is empty now", e);
        }
    }
}
