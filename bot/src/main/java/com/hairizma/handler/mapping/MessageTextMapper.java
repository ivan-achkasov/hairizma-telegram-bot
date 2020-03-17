package com.hairizma.handler.mapping;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;

@Component
public class MessageTextMapper implements Mapper<MessageTextMapping> {

	@Override
	public boolean satisfied(final Update update, final MessageTextMapping annotation) {
		if(update == null) {
			return false;
		}
		final Message message;
		if((message = update.getMessage()) == null) {
			return false;
		}

		return StringUtils.equals(annotation.value()[0], message.getText());
	}

	@Override
	public Class<MessageTextMapping> getAnnotationClass() {
		return MessageTextMapping.class;
	}

}
