package com.hairizma.handler;

import com.hairizma.bot.MessagesSender;
import com.hairizma.internationalisation.Message;
import com.hairizma.internationalisation.MessagesResolver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageNotRecognizedHandler implements UpdateHandler {

    private final MessagesResolver messagesResolver;

    public MessageNotRecognizedHandler(final MessagesResolver messagesResolver) {
        this.messagesResolver = messagesResolver;
    }

    @Override
    public void handleUpdate(final Update update, final MessagesSender messagesSender) throws Exception {
        final long chatId = update.getMessage().getChatId();
        messagesSender.sendText(chatId, messagesResolver.getMessage(chatId, Message.COMMON_NOT_RECOGNIZED_MESSAGE));
    }
}
