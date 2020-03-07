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

    private final MessagesSender messagesSender;

    public MainBot(final UpdateRegulator updateRegulator) {
        this.updateRegulator = updateRegulator;
        this.messagesSender = new MessagesSender(this);
    }

    @Override
    public void onUpdateReceived(final Update update) {
        updateRegulator.resolve(MainBot.class, update, messagesSender);
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
