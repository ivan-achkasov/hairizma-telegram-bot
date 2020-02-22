package com.hairizma.bot;

import com.hairizma.handler.StartMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateResolver {

    @Autowired
    private StartMessageHandler startMessageHandler;

    public void resolve(final Update update, final MessagesSender messagesSender) {
        if("/start".equals(update.getMessage().getText())) {
            startMessageHandler.handleUpdate(update, messagesSender);
        }
    }
}
