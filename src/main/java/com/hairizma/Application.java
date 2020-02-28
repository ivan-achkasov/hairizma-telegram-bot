package com.hairizma;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;

public class Application {


    public static void main(String[] args) {
        ApiContextInitializer.init();

        new ClassPathXmlApplicationContext("applicationContext.xml");

        PropertyConfigurator.configure(Application.class.getClassLoader().getResourceAsStream("log4j-default.properties"));
    }

}
