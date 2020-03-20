package com.hairizma.bot;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessagesSender {

    private final DefaultAbsSender sender;

    public MessagesSender(final DefaultAbsSender sender) {
        this.sender = sender;
    }

    public Message sendText(final long chatId, final String text) throws TelegramApiException {
        return send(new SendMessage(chatId, text));
    }

    public Message send(final SendMessage sendObject) throws TelegramApiException {
        return sender.execute(sendObject);
    }

    public Boolean sendCallbackAnswerAlert(final String callbackId, final String text) throws TelegramApiException {
        return send(new AnswerCallbackQuery().setCallbackQueryId(callbackId).setText(text).setShowAlert(true));
    }

    public Boolean sendCallbackAnswerPopup(final String callbackId, final String text) throws TelegramApiException {
        return send(new AnswerCallbackQuery().setCallbackQueryId(callbackId).setText(text));
    }

    public Boolean send(final AnswerCallbackQuery answerCallbackQuery) throws TelegramApiException {
        return sender.execute(answerCallbackQuery);
    }

}
