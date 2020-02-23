package com.hairizma;

import com.hairizma.bot.MainBot;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

public class Application {

    private static ApplicationContext applicationContext;
    private static TelegramBotsApi telegramBotsApi;

    public static void main(String[] args) {
        init();

        MainBot mainBot = applicationContext.getBean(MainBot.class);
        registerBot(mainBot);
    }

    private static void init() {
        ApiContextInitializer.init();
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        telegramBotsApi = new TelegramBotsApi();
        PropertyConfigurator.configure(
                Application.class.getClassLoader().getResourceAsStream("log4j-default.properties"));
    }

    private static void registerBot(LongPollingBot bot) {
        if(bot == null) {
            return;
        }
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
