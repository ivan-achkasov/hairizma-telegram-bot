package com.hairizma.bot;

import com.hairizma.exception.DefaultExceptionResolver;
import com.hairizma.exception.ExceptionResolver;
import com.hairizma.handler.Handler;
import com.hairizma.handler.StartMessageHandler;
import com.hairizma.handler.UpdateHandler;
import com.hairizma.handler.mapping.Mapper;
import com.hairizma.handler.mapping.MessageTextMapper;
import com.hairizma.handler.mapping.MessageTextMapping;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UpdateRegulator {

    private final List<UpdateHandler> updateHandlers;
    private final Collection<ExceptionResolver> exceptionResolvers;
    private final DefaultExceptionResolver defaultExceptionResolver;

    public UpdateRegulator(final List<UpdateHandler> updateHandlers,
                          final Collection<ExceptionResolver> exceptionResolvers,
                          final DefaultExceptionResolver defaultExceptionResolver) {
        this.updateHandlers = updateHandlers;
        this.exceptionResolvers = exceptionResolvers;
        this.defaultExceptionResolver = defaultExceptionResolver;
    }

    public void resolve(final Class botIdentifier, final Update update, final MessagesSender messagesSender) {
        if(botIdentifier == null || update == null) {
            return;
        }
        try {
            for (UpdateHandler updateHandler : updateHandlers) {
                if(botIdentifier.equals(getBotIdentifier(updateHandler)) && isMappingSatisfied(update, updateHandler)) {
                    updateHandler.handleUpdate(update, messagesSender);
                }
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

    private Class getBotIdentifier(final UpdateHandler updateHandler) {
        final Handler[] annotations = updateHandler.getClass().getAnnotationsByType(Handler.class);
        if(annotations.length > 0) {
            return annotations[0].value();
        }
        return null;
    }

    private boolean isMappingSatisfied(final Update update, final UpdateHandler updateHandler) {
        final Collection<Mapper> mappers = getMappers(updateHandler);

        for(Mapper mapper : mappers) {
            if (mapper.satisfied(update)){
                return  true;
            }
        }

        return false;
    }

    private Collection<Mapper> getMappers(final UpdateHandler updateHandler) {
        final Annotation[] annotations = updateHandler.getClass().getAnnotations();
        if (annotations.length < 1) {
            return Collections.emptySet();
        }

        final Set<Mapper> mappers = new HashSet<>();

        for (Annotation annotation : annotations) {
            if (annotation instanceof MessageTextMapping) {
                final MessageTextMapping mappingAnnotation = (MessageTextMapping) annotation;

                for (final String text : mappingAnnotation.value()) {
                    mappers.add(new MessageTextMapper(text));
                }
            }
        }

        return mappers;
    }
}
