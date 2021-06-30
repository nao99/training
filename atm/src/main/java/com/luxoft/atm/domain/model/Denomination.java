package com.luxoft.atm.domain.model;

/**
 * Denomination enum
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-29
 */
public enum Denomination {
    DENOMINATION_50,
    DENOMINATION_100,
    DENOMINATION_500,
    DENOMINATION_1000,
    DENOMINATION_2000,
    DENOMINATION_5000;

    public int toInt() {
        switch (ordinal()) {
            case 0:
                return 50;
            case 1:
                return 100;
            case 2:
                return 500;
            case 3:
                return 1000;
            case 4:
                return 2000;
            case 5:
                return 5000;
            default:
                throw new RuntimeException("Unexpected exception");
        }
    }

    public boolean lessThan(Denomination denomination) {
        return toInt() < denomination.toInt();
    }
}
