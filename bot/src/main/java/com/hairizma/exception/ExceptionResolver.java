package com.hairizma.exception;

import com.hairizma.bot.MessagesManager;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ExceptionResolver {

    void resolve(Exception e, Update update, MessagesManager messagesManager);

    boolean isAssignable(Exception e, Update update);
}
