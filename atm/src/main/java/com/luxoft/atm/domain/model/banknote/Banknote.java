package com.luxoft.atm.domain.model.banknote;

import com.luxoft.atm.domain.model.Denomination;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Banknote class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-29
 */
@Getter
@Builder
public class Banknote implements Serializable {
    private final Denomination denomination;
    private final UUID uuid;

    private Banknote(Denomination denomination, UUID uuid) throws IllegalArgumentException {
        if (denomination == null) {
            throw new IllegalArgumentException("Banknote's denomination must not be null");
        }

        this.denomination = denomination;
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    }

    @Override
    public String toString() {
        return "Banknote{" +
            "denomination=" + denomination +
            ", uuid=" + uuid +
            '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Banknote banknote = (Banknote) other;

        return Objects.equals(uuid, banknote.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
