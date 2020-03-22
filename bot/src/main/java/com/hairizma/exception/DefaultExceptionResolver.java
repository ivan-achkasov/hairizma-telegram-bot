package com.hairizma.exception;

import com.hairizma.bot.BotUtils;
import com.hairizma.bot.MessagesManager;
import com.hairizma.internationalisation.Message;
import com.hairizma.internationalisation.MessagesResolver;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class DefaultExceptionResolver implements ExceptionResolver {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionResolver.class);

    private final MessagesResolver messagesResolver;

    public DefaultExceptionResolver(final MessagesResolver messagesResolver) {
        this.messagesResolver = messagesResolver;
    }

    @Override
    public void resolve(final Exception e, final Update update, final MessagesManager messagesManager) {
        final long chatId = BotUtils.getChatId(update);
        try {
            messagesManager.sendText(chatId, messagesResolver.getMessage(chatId, Message.COMMON_ERROR_DEFAULT));
            LOGGER.error("Exception occurred. Default error message has been sent to the user {}. Exception:",
                    chatId, e);
        } catch (Exception ex) {
            LOGGER.error("Could not send error message to the user {}. Source exception {} Exception:",
                    chatId, ExceptionUtils.getStackTrace(e), ex);
        }
    }

    @Override
    public boolean isAssignable(final Exception e, final Update update) {
        return true;
    }
}
