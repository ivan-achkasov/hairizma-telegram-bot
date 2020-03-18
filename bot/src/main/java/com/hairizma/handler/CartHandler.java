package com.hairizma.handler;

import com.hairizma.bot.BotUtils;
import com.hairizma.bot.MainBot;
import com.hairizma.bot.MessagesSender;
import com.hairizma.cart.CartService;
import com.hairizma.handler.mapping.CallbackQueryValueMapping;
import org.apache.commons.lang3.math.NumberUtils;
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
        final String callbackData = update.getCallbackQuery().getData();
        final int productId = NumberUtils.toInt(callbackData.substring(ADD_TO_CART_CALLBACK_PREFIX.length()));
        if(productId <= 0) {
            return;
        }

        final long chatId = BotUtils.getChatId(update);

        boolean added = cartService.addProduct(chatId, productId);
        if(added) {
            messagesSender.sendText(chatId, "Товар добавлен.");
        } else {
            messagesSender.sendText(chatId, "Товар уже был добавлен.");
        }
    }

}
