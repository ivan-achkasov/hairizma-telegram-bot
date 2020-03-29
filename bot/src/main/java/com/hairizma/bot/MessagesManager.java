package com.hairizma.bot;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

    public Message sendText(final long chatId, final String text) throws SendException {
        return send(new SendMessage(chatId, text));
    }

    public Message send(final SendMessage sendObject) throws SendException {
        try {
            return sender.execute(sendObject);
        } catch (TelegramApiException e) {
            throw new SendException(e, sendObject.getChatId());
        }
    }

    public boolean sendCallbackAnswerAlert(final String callbackId, final String text) throws SendException {
        return ObjectUtils.defaultIfNull(
                send(new AnswerCallbackQuery().setCallbackQueryId(callbackId).setText(text).setShowAlert(true)),
                false);
    }

    public boolean sendCallbackAnswerPopup(final String callbackId, final String text) throws SendException {
        return ObjectUtils.defaultIfNull(
                send(new AnswerCallbackQuery().setCallbackQueryId(callbackId).setText(text)),
                false);
    }

    public boolean send(final AnswerCallbackQuery answerCallbackQuery) throws SendException {
        try {
            return ObjectUtils.defaultIfNull(
                    sender.execute(answerCallbackQuery),
                    false);
        } catch (TelegramApiException e) {
            throw new SendException(e);
        }
    }

    public boolean deleteMessage(final long chatId, int messageId) throws SendException {
        try {
            return ObjectUtils.defaultIfNull(
                    sender.execute(new DeleteMessage(chatId, messageId)),
                    false);
        } catch (TelegramApiException e) {
            throw new SendException(e, chatId);
        }
    }

    public void edit(final EditMessageText editMessage) throws SendException {
        try {
            sender.execute(editMessage);
        } catch (TelegramApiException e) {
            throw new SendException(e, editMessage.getChatId());
        }
    }
}
