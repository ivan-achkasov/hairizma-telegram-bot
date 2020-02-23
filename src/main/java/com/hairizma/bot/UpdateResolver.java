package com.hairizma.bot;

import com.hairizma.exception.DefaultExceptionResolver;
import com.hairizma.exception.ExceptionResolver;
import com.hairizma.handler.StartMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;

@Component
public class UpdateResolver {

    private final StartMessageHandler startMessageHandler;
    private final Collection<ExceptionResolver> exceptionResolvers;
    private final DefaultExceptionResolver defaultExceptionResolver;

    public UpdateResolver(final StartMessageHandler startMessageHandler,
                          final Collection<ExceptionResolver> exceptionResolvers,
                          final DefaultExceptionResolver defaultExceptionResolver) {
        this.startMessageHandler = startMessageHandler;
        this.exceptionResolvers = exceptionResolvers;
        this.defaultExceptionResolver = defaultExceptionResolver;
    }

    public void resolve(final Update update, final MessagesSender messagesSender) {
        try {
            if ("/start".equals(update.getMessage().getText())) {
                startMessageHandler.handleUpdate(update, messagesSender);
            }
        } catch (final Exception e) {
            getExceptionResolver(e, update).resolve(e, update, messagesSender);
        }
    }

    private ExceptionResolver getExceptionResolver(final Exception e, final Update update) {
        for (ExceptionResolver exceptionResolver : exceptionResolvers) {
            if(exceptionResolver.isAssignable(e, update)) {
                return exceptionResolver;
            }
        }
        return defaultExceptionResolver;
    }
}
