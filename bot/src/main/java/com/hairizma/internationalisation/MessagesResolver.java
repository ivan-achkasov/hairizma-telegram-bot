package com.hairizma.internationalisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessagesResolver {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localResolver;

    protected String getMessage(final long chatId, final String code, final Object... args) {
        return messageSource.getMessage(code, args, localResolver.getLocale(chatId));
    }

    public String getMessage(final long chatId, final Message message, final Object... args) {
        return getMessage(chatId, message.getCode(), args);
    }

}
