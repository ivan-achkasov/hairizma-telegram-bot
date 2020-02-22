package com.hairizma.local;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleResolver {

    public Locale getLocale(final long chatId) {
        return Locale.forLanguageTag("ru");
    }
}
