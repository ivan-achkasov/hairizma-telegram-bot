package com.hairizma.handler;

import com.hairizma.bot.MessagesSender;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {

    void handleUpdate(Update update, MessagesSender messagesSender) throws Exception;

}
