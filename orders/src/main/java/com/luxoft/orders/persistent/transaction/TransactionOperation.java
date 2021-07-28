package com.luxoft.orders.persistent.transaction;

import org.hibernate.Session;

import java.util.function.Function;

/**
 * TransactionOperation interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-20
 */
public interface TransactionOperation<T> extends Function<Session, T> {
}
