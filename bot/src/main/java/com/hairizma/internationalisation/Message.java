package com.hairizma.internationalisation;

import java.util.Objects;

public enum Message {
    COMMON_ERROR_DEFAULT("common.error.default"),
    COMMON_NOT_RECOGNIZED_MESSAGE("common.message.not_recognized"),
    START_WELCOME("start.welcome");

    private final String code;

    private Message(final String code) {
        this.code = Objects.requireNonNull(code);
    }

    public String getCode() {
        return code;
    }
}
