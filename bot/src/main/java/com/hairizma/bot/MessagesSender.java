package com.hairizma.bot;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessagesSender {

    private final DefaultAbsSender sender;

    public MessagesSender(final DefaultAbsSender sender) {
        this.sender = sender;
    }

    public Message sendText(final long chatId, final String text) throws TelegramApiException {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return send(sendMessage);
    }

    public Message send(final SendMessage sendObject) throws TelegramApiException {
        return sender.execute(sendObject);
    }

}
