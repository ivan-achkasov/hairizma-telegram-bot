package com.hairizma.bot;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Bot
public class MainBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    private final UpdateRegulator updateRegulator;

    private final MessagesManager messagesManager;

    public MainBot(final UpdateRegulator updateRegulator) {
        this.updateRegulator = updateRegulator;
        this.messagesManager = new MessagesManager(this);
    }

    @Override
    public void onUpdateReceived(final Update update) {
        updateRegulator.resolve(MainBot.class, update, messagesManager);
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
