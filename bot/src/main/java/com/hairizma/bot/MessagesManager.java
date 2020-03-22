package com.hairizma.bot;

import org.apache.commons.lang3.ObjectUtils;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessagesManager {

    private final DefaultAbsSender sender;

    public MessagesManager(final DefaultAbsSender sender) {
        this.sender = sender;
    }

    public Message sendText(final long chatId, final String text) throws TelegramApiException {
        return send(new SendMessage(chatId, text));
    }

    public Message send(final SendMessage sendObject) throws TelegramApiException {
        return sender.execute(sendObject);
    }

    public boolean sendCallbackAnswerAlert(final String callbackId, final String text) throws TelegramApiException {
        return ObjectUtils.defaultIfNull(
                send(new AnswerCallbackQuery().setCallbackQueryId(callbackId).setText(text).setShowAlert(true)),
                false);
    }

    public boolean sendCallbackAnswerPopup(final String callbackId, final String text) throws TelegramApiException {
        return ObjectUtils.defaultIfNull(
                send(new AnswerCallbackQuery().setCallbackQueryId(callbackId).setText(text)),
                false);
    }

    public boolean send(final AnswerCallbackQuery answerCallbackQuery) throws TelegramApiException {
        return ObjectUtils.defaultIfNull(
                sender.execute(answerCallbackQuery),
                false);
    }

    public boolean deleteMessage(final long chatId, int messageId) throws TelegramApiException {
        return ObjectUtils.defaultIfNull(
                sender.execute(new DeleteMessage(chatId, messageId)),
                false);
    }

    public void edit(final EditMessageText editMessage) throws TelegramApiException {
        sender.execute(editMessage);
    }
}
