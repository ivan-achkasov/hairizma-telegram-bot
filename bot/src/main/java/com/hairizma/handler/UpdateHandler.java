package com.hairizma.handler;

import com.hairizma.bot.MessagesManager;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {

    void handleUpdate(Update update, MessagesManager messagesManager) throws Exception;

}
