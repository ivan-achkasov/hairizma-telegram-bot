package com.hairizma.handler;

import com.hairizma.bot.MessagesManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class HandlerManager {

    private final Map<Long, BiConsumer<Message, MessagesManager>> fixedHandlers = new HashMap<>();

    public synchronized <F extends BiConsumer<Message, MessagesManager>> void fixHandleConsumer(final long chatId, final F handleMethod) {
        fixedHandlers.put(chatId, handleMethod);
    }

    public synchronized boolean applyFixedConsumer(final Message message, final MessagesManager messagesManager) {
        if(message == null) {
            return false;
        }
        final BiConsumer<Message, MessagesManager> consumer = fixedHandlers.remove(message.getChatId());
        if(consumer == null) {
            return false;
        }
        consumer.accept(message, messagesManager);
        return true;
    }

}
