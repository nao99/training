package ru.otus.time;

import java.time.LocalDateTime;

/**
 * CurrentTimeDefinerImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class CurrentTimeDefinerImpl implements CurrentTimeDefiner {
    @Override
    public LocalDateTime time() {
        return LocalDateTime.now();
    }
}
