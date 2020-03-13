package com.hairizma.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

public class BotUtils {
    public static final int DEFAULT_CHAT_ID = -1;
    public static long getChatId(final Update update) {
        if (update == null) {
            return DEFAULT_CHAT_ID;
        }

        if(update.hasMessage()) {
            return update.getMessage().getChatId();
        }

        return DEFAULT_CHAT_ID;
    }
}
