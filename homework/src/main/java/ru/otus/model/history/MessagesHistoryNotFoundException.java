package ru.otus.model.history;

/**
 * MessagesHistoryNotFoundException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class MessagesHistoryNotFoundException extends MessagesHistoryException {
    public MessagesHistoryNotFoundException(String message) {
        super(message);
    }
}
