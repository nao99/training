package ru.otus.time;

/**
 * CurrentSecondsDefiner class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class CurrentSecondsDefiner {
    public int define() {
        // todo: write test for it
        return (int) (System.currentTimeMillis() / 1000);
    }
}
