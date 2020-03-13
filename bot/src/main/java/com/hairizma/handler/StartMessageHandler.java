package com.hairizma.handler;

import com.hairizma.bot.BotUtils;
import com.hairizma.bot.MainBot;
import com.hairizma.bot.MessagesSender;
import com.hairizma.handler.mapping.MessageTextMapping;
import com.hairizma.internationalisation.Message;
import com.hairizma.internationalisation.MessagesResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Handler(MainBot.class)
@MessageTextMapping("/start")
public class StartMessageHandler implements UpdateHandler {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(StartMessageHandler.class);

    private final MessagesResolver messagesResolver;

    public StartMessageHandler(MessagesResolver messagesResolver) {
        this.messagesResolver = messagesResolver;
    }

    @Override
    public void handleUpdate(final Update update, final MessagesSender messagesSender) throws Exception {
        final long chatId = BotUtils.getChatId(update);
        final String welcomeText = getWelcomeMessage(chatId);
        final SendMessage sendMessage = new SendMessage(chatId, welcomeText);
        sendMessage.setReplyMarkup(getKeyboard());

        messagesSender.send(sendMessage);

        LOGGER.info("Welcome message sent to user in chat {}.", chatId);
    }

    private ReplyKeyboard getKeyboard() {
        final List<KeyboardRow> keyboard = new ArrayList<>();
        final ReplyKeyboardMarkup replyKeyboardMarkup =
                new ReplyKeyboardMarkup(keyboard)
                        .setSelective(true)
                        .setResizeKeyboard(true)
                        .setOneTimeKeyboard(false);


        final KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Каталог"));
        keyboardFirstRow.add(new KeyboardButton("Помощь"));
        keyboard.add(keyboardFirstRow);

        return replyKeyboardMarkup;
    }

    private String getWelcomeMessage(final long chatId) {
        return messagesResolver.getMessage(chatId, Message.START_WELCOME);
    }
}
