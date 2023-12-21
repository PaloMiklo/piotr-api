package com.api.piotr.util;

public enum Duration {
    LEVEL_I(110L),
    LEVEL_II(160L);

    private final long value;

    Duration(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
