package com.hairizma.handler;

import com.hairizma.bot.MessagesSender;
import com.hairizma.local.MessagesResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class StartMessageHandler implements UpdateHandler {

    @Autowired
    private MessagesResolver messagesResolver;

    @Override
    public void handleUpdate(final Update update, final MessagesSender messagesSender) {
        final long chatId = update.getMessage().getChatId();
        try {
            messagesSender.sendText(chatId, getWelcomeMessage(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getWelcomeMessage(final long chatId) {
        return messagesResolver.getMessage(chatId, "start.welcome");
    }
}
