package com.hairizma.handler.mapping;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageTextMapper implements Mapper {

	private final String messageText;

	public MessageTextMapper(final String messageText) {
		this.messageText = messageText;
	}

	@Override
	public boolean satisfied(final Update update) {
		if(update == null) {
			return false;
		}
		final Message message;
		if((message = update.getMessage()) == null) {
			return false;
		}

		return StringUtils.equals(messageText, message.getText());
	}

}
