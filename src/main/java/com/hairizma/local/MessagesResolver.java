package com.hairizma.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessagesResolver {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localResolver;

    public String getMessage(final long chatId, final String code, final Object... args) {
        return messageSource.getMessage(code, args, localResolver.getLocale(chatId));
    }

}
