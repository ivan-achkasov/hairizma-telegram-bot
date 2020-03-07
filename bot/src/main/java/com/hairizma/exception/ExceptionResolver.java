package com.hairizma.exception;

import com.hairizma.bot.MessagesSender;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ExceptionResolver {

    void resolve(Exception e, Update update, MessagesSender messagesSender);

    boolean isAssignable(Exception e, Update update);
}
