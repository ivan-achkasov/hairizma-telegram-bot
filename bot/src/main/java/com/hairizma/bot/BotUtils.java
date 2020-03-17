package com.hairizma.bot;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;

public class BotUtils {
    public static final int DEFAULT_CHAT_ID = -1;

    private static final String HIDDEN_IMAGE_URL_PATTERN = "[\u2060]({0}/image/{1})";

    public static long getChatId(final Update update) {
        if (update == null) {
            return DEFAULT_CHAT_ID;
        }

        if(update.hasMessage()) {
            return update.getMessage().getChatId();
        }

        return DEFAULT_CHAT_ID;
    }

    public static String getHiddenImageLink(final String serviceHost, final long imageId) {
        if(StringUtils.isBlank(serviceHost) || imageId <= 0) {
            return "";
        }
        return MessageFormat.format(HIDDEN_IMAGE_URL_PATTERN, serviceHost, imageId);
    }
}
