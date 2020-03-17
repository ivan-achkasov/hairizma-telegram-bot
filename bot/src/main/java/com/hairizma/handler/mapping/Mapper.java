package com.hairizma.handler.mapping;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;

public interface Mapper<T extends Annotation> {

	boolean satisfied(final Update update, T annotation);

	Class<T> getAnnotationClass();

}
