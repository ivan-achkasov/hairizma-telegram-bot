package com.hairizma.handler;

import com.hairizma.bot.MainBot;
import com.hairizma.bot.MessagesSender;
import com.hairizma.handler.mapping.MessageTextMapping;
import com.hairizma.internationalisation.Message;
import com.hairizma.internationalisation.MessagesResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Handler(MainBot.class)
@MessageTextMapping("/start")
public class StartMessageHandler implements UpdateHandler {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(StartMessageHandler.class);

    private final MessagesResolver messagesResolver;

    public StartMessageHandler(MessagesResolver messagesResolver) {
        this.messagesResolver = messagesResolver;
    }

    @Override
    public void handleUpdate(final Update update, final MessagesSender messagesSender) throws Exception {
        final long chatId = update.getMessage().getChatId();
        messagesSender.sendText(chatId, getWelcomeMessage(chatId));

        LOGGER.info("Welcome message sent to user in chat {}.", chatId);
    }

    private String getWelcomeMessage(final long chatId) {
        return messagesResolver.getMessage(chatId, Message.START_WELCOME);
    }
}
