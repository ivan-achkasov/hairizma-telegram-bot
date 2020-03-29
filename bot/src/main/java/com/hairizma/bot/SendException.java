package com.hairizma.bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SendException extends RuntimeException {

    private final String chatId;

    public SendException(final TelegramApiException e) {
       this(e, "");
    }

    public SendException(final TelegramApiException e, final long chatId) {
        this(e, Long.toString(chatId));
    }

    public SendException(final TelegramApiException e, final String chatId) {
        super(e);
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }
}
