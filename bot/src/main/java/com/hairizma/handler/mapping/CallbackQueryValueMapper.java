package com.hairizma.handler.mapping;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Pattern;

@Component
public class CallbackQueryValueMapper implements Mapper<CallbackQueryValueMapping> {

    @Override
    public boolean satisfied(final Update update, final CallbackQueryValueMapping annotation) {
        final CallbackQuery callbackQuery = update.getCallbackQuery();
        if(callbackQuery == null) {
            return false;
        }

        final String value = callbackQuery.getData();
        if(value == null) {
            return false;
        }

        return Pattern.compile(annotation.value()[0]).matcher(value).matches();
    }

    @Override
    public Class<CallbackQueryValueMapping> getAnnotationClass() {
        return CallbackQueryValueMapping.class;
    }

}
