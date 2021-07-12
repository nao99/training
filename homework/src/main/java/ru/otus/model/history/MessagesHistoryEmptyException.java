package ru.otus.model.history;

/**
 * MessagesHistoryEmptyException class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class MessagesHistoryEmptyException extends MessagesHistoryException {
    public MessagesHistoryEmptyException(String message, Throwable previous) {
        super(message, previous);
    }
}
