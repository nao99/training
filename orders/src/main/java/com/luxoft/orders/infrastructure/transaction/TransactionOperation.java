package com.luxoft.orders.infrastructure.transaction;

import java.sql.Connection;
import java.util.function.Function;

/**
 * TransactionOperation interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-20
 */
public interface TransactionOperation<T> extends Function<Connection, T> {
}
