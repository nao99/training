package ru.otus.model.history;

/**
 * MessagesHistoryException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class MessagesHistoryException extends RuntimeException {
    public MessagesHistoryException(String message) {
        super(message);
    }

    public MessagesHistoryException(String message, Throwable previous) {
        super(message, previous);
    }
}
