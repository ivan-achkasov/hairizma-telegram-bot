package com.hairizma.handler.mapping;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Mapper {

	boolean satisfied(Update update);

}
