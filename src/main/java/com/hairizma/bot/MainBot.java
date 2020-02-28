package com.hairizma.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Bot
@Component
public class MainBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    private UpdateResolver updateResolver;

    private final MessagesSender messagesSender;

    public MainBot() {
        this.messagesSender = new MessagesSender(this);
    }

    @Override
    public void onUpdateReceived(final Update update) {
        updateResolver.resolve(update, messagesSender);
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
