package com.hairizma.bot;

import com.hairizma.handler.HandlerManager;
import com.hairizma.handler.UpdateHandler;
import com.hairizma.exception.DefaultExceptionResolver;
import com.hairizma.exception.ExceptionResolver;
import com.hairizma.handler.Handler;
import com.hairizma.handler.mapping.Mapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.util.*;

@Component
public class UpdateRegulator {

    private final List<UpdateHandler> updateHandlers;
    private final Collection<ExceptionResolver> exceptionResolvers;
    private final DefaultExceptionResolver defaultExceptionResolver;
    private final List<Mapper<?>> mappers;
    private final HandlerManager handlerManager;

    public UpdateRegulator(final List<UpdateHandler> updateHandlers,
                           final Collection<ExceptionResolver> exceptionResolvers,
                           final DefaultExceptionResolver defaultExceptionResolver,
                           final List<Mapper<?>> mappers,
                           final HandlerManager handlerManager) {
        this.updateHandlers = updateHandlers;
        this.exceptionResolvers = exceptionResolvers;
        this.defaultExceptionResolver = Objects.requireNonNull(defaultExceptionResolver);
        this.mappers = mappers;
        this.handlerManager = Objects.requireNonNull(handlerManager);
    }

    public void resolve(final Class<?> botIdentifier, final Update update, final MessagesManager messagesManager) {
        if(botIdentifier == null || update == null) {
            return;
        }
        try {
            if(update.hasMessage() && handlerManager.applyFixedConsumer(update.getMessage(), messagesManager)) {
                return;
            }
            final UpdateHandler updateHandler = findUpdateHandler(botIdentifier, update);
            if(updateHandler != null) {
                updateHandler.handleUpdate(update, messagesManager);
            }
        } catch (final Exception e) {
            getExceptionResolver(e, update).resolve(e, update, messagesManager);
        }
    }

    private UpdateHandler findUpdateHandler(final Class<?> botIdentifier, final Update update) {
        for (UpdateHandler updateHandler : updateHandlers) {
            if(botIdentifier.equals(getBotIdentifier(updateHandler)) && isMappingSatisfied(update, updateHandler)) {
                return updateHandler;
            }
        }
        return null;
    }

    private ExceptionResolver getExceptionResolver(final Exception e, final Update update) {
        for (ExceptionResolver exceptionResolver : exceptionResolvers) {
            if(exceptionResolver.isAssignable(e, update)) {
                return exceptionResolver;
            }
        }
        return defaultExceptionResolver;
    }

    private Class<?> getBotIdentifier(final UpdateHandler updateHandler) {
        final Handler[] annotations = updateHandler.getClass().getAnnotationsByType(Handler.class);
        if(annotations.length > 0) {
            return annotations[0].value();
        }
        return null;
    }

    private boolean isMappingSatisfied(final Update update, final UpdateHandler updateHandler) {
        final Annotation[] annotations = updateHandler.getClass().getAnnotations();
        if (annotations.length < 1) {
            return false;
        }

        for (Annotation annotation : annotations) {
            for (Mapper<? extends Annotation> mapper : this.mappers) {
                if(isMappingSatisfied(update, mapper, annotation)) {
                    return true;
                }
            }
        }

        return false;
    }

    private <T extends Annotation> boolean isMappingSatisfied(final Update update,
                                                              final Mapper<T> mapper,
                                                              final Annotation annotation) {
        if(!mapper.getAnnotationClass().isInstance(annotation)){
            return false;
        }
       return mapper.satisfied(update, (T)annotation);
    }
}
