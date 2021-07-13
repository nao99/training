package ru.otus.time;

import java.time.LocalDateTime;

/**
 * CurrentTimeDefiner interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-13
 */
public interface CurrentTimeDefiner {
    /**
     * Gets the current time
     *
     * @return the current time
     */
    LocalDateTime time();
}
