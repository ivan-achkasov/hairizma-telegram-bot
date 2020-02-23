package com.hairizma.internationalisation;

import java.util.Objects;

public enum Message {
    COMMON_ERROR_DEFAULT("common.error.default"),
    START_WELCOME("start.welcome");

    private final String code;

    private Message(final String code) {
        this.code = Objects.requireNonNull(code);
    }

    public String getCode() {
        return code;
    }
}
