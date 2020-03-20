package com.hairizma.handler;

import com.hairizma.bot.BotUtils;
import com.hairizma.bot.MainBot;
import com.hairizma.bot.MessagesSender;
import com.hairizma.cart.CartService;
import com.hairizma.handler.mapping.CallbackQueryValueMapping;
import org.apache.commons.lang3.math.NumberUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Handler(MainBot.class)
@CallbackQueryValueMapping(CartHandler.ADD_TO_CART_CALLBACK_PREFIX + "\\d+")
public class CartHandler implements UpdateHandler {

    public static final String ADD_TO_CART_CALLBACK_PREFIX = "addProductToCart:";

    private final CartService cartService;

    public CartHandler(final CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void handleUpdate(Update update, MessagesSender messagesSender) throws Exception {
        if(!update.hasCallbackQuery()) {
            return;
        }
        final CallbackQuery callbackQuery = update.getCallbackQuery();
        final String callbackData = callbackQuery.getData();
        final int productId = NumberUtils.toInt(callbackData.substring(ADD_TO_CART_CALLBACK_PREFIX.length()));
        if(productId <= 0) {
            return;
        }

        final long chatId = BotUtils.getChatId(update);
        final String callbackQueryId = callbackQuery.getId();

        boolean added = cartService.addProduct(chatId, productId);
        if(added) {
            messagesSender.sendCallbackAnswerPopup(callbackQueryId, "Товар добавлен.");
        } else {
            messagesSender.sendCallbackAnswerPopup(callbackQueryId, "Товар уже был добавлен.");
        }
    }

}
