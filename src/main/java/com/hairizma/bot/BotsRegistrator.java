package com.hairizma.bot;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.WebhookBot;

public class BotsRegistrator implements InitializingBean {

	private final TelegramBotsApi telegramBotsApi;
	private final ListableBeanFactory beanFactory;

	public BotsRegistrator(final TelegramBotsApi telegramBotsApi, final ListableBeanFactory beanFactory) {
		this.telegramBotsApi = telegramBotsApi;
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final Map<String, Object> beans = beanFactory.getBeansWithAnnotation(Bot.class);
		for (Object botObject : beans.values()) {
			if(botObject instanceof LongPollingBot) {
				telegramBotsApi.registerBot((LongPollingBot)botObject);
			} else if(botObject instanceof WebhookBot) {
				telegramBotsApi.registerBot((WebhookBot)botObject);
			}
		}
	}
}
