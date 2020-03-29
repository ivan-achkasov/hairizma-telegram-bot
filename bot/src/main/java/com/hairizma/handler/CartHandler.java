package com.hairizma.handler;

import com.hairizma.bot.BotUtils;
import com.hairizma.bot.MainBot;
import com.hairizma.bot.MessagesManager;
import com.hairizma.dto.Product;
import com.hairizma.service.ProductService;
import com.hairizma.service.cart.Cart;
import com.hairizma.service.cart.CartService;
import com.hairizma.handler.mapping.CallbackQueryValueMapping;
import com.hairizma.handler.mapping.MessageTextMapping;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.function.BiFunction;

@Handler(MainBot.class)
@MessageTextMapping(CartHandler.OPEN_CART_QUERY)
@CallbackQueryValueMapping({
        CartHandler.ADD_TO_CART_CALLBACK_PREFIX + "\\d+",
        CartHandler.REMOVE_FROM_CART_CALLBACK_PREFIX + "\\d+",
        CartHandler.PUSH_TO_CART_CALLBACK_PREFIX + "\\d+",
        CartHandler.POP_FROM_CART_CALLBACK_PREFIX + "\\d+"})
public class CartHandler implements UpdateHandler {

    public static final String OPEN_CART_QUERY = "Открыть корзину";
    public static final String ADD_TO_CART_CALLBACK_PREFIX = "addProductToCart:";
    public static final String REMOVE_FROM_CART_CALLBACK_PREFIX = "removeProductFromCart:";
    public static final String PUSH_TO_CART_CALLBACK_PREFIX = "pushProductToCart:";
    public static final String POP_FROM_CART_CALLBACK_PREFIX = "popProductFromCart:";

    private final String servicesHost;

    private final CartService cartService;
    private final ProductService productService;

    public CartHandler(final CartService cartService,
                       final ProductService productService,
                       @Value("${services.host}") final String servicesHost) {
        this.cartService = Objects.requireNonNull(cartService);
        this.productService = Objects.requireNonNull(productService);
        this.servicesHost = servicesHost;
    }

    @Override
    public void handleUpdate(Update update, MessagesManager messagesManager) throws Exception {
        if(update.hasMessage()) {
            processMessage(update.getMessage(), messagesManager);
        } else if (update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery(), messagesManager);
        }
    }

    private void processMessage(final Message message, final MessagesManager messagesManager) {
        final String messageText = message.getText();
        if(OPEN_CART_QUERY.equals(messageText)) {
            openCart(message.getChatId(), messagesManager);
        }
    }

    private void processCallbackQuery(final CallbackQuery callbackQuery, final MessagesManager messagesManager) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final String callbackData = callbackQuery.getData();
        int productId;
        if(callbackData.startsWith(ADD_TO_CART_CALLBACK_PREFIX)) {
            productId = getProductId(callbackData, ADD_TO_CART_CALLBACK_PREFIX);
            addToCart(chatId, callbackQuery.getId(), productId, messagesManager);
        } else if (callbackData.startsWith(PUSH_TO_CART_CALLBACK_PREFIX)) {
            productId = getProductId(callbackData, PUSH_TO_CART_CALLBACK_PREFIX);
            changeProductCountInCart(
                    chatId, callbackQuery.getId(), productId,
                    callbackQuery.getMessage().getMessageId(),
                    cartService::pushProduct, messagesManager);
        } else if (callbackData.startsWith(POP_FROM_CART_CALLBACK_PREFIX)) {
            productId = getProductId(callbackData, POP_FROM_CART_CALLBACK_PREFIX);
            changeProductCountInCart(
                    chatId, callbackQuery.getId(), productId,
                    callbackQuery.getMessage().getMessageId(),
                    cartService::popProduct, messagesManager);
        } else if (callbackData.startsWith(REMOVE_FROM_CART_CALLBACK_PREFIX)) {
            productId = getProductId(callbackData, REMOVE_FROM_CART_CALLBACK_PREFIX);;
            changeProductCountInCart(
                    chatId, callbackQuery.getId(), productId,
                    callbackQuery.getMessage().getMessageId(),
                    (c, p) -> {cartService.removeProduct(c, p); return 0;}, messagesManager);
        }
    }

    private void openCart(final long chatId, final MessagesManager messagesManager) {
        final Cart cart = cartService.getCart(chatId);
        if(cart.isEmpty()) {
            messagesManager.sendText(chatId, "Корзина пуста. Выберите хотябы один товар.");
            return;
        }
        final Map<Integer, Integer> productsCount = cart.getProductsCount();
        final List<Product> products = productService.getProducts(productsCount.keySet());
        for(Product product : products) {
            final int count = ObjectUtils.defaultIfNull(productsCount.get(product.getId()), 0);
            final String messageText = generateCartItemInfo(product, count);
            final ReplyKeyboard buttons = getButtonsForCartView(product, count);
            final SendMessage sendMessage = new SendMessage(chatId, messageText)
                    .setParseMode("Markdown")
                    .setReplyMarkup(buttons);
            messagesManager.send(sendMessage);
        }
    }

    private String generateCartItemInfo(final Product product, final int count) {
        String message = "*" + product.getName() + "*. "
                + "Кол-во: " + count;
        if(product.getImageId() > 0) {
            message += BotUtils.getHiddenImageLink(servicesHost, product.getImageId());
        }
        return message;
    }

    private InlineKeyboardMarkup getButtonsForCartView(final Product product, final int productCount){
        final InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        markupKeyboard.setKeyboard(buttons);

        final List<InlineKeyboardButton> firstLine = new ArrayList<>();
        buttons.add(firstLine);

        firstLine.add(new InlineKeyboardButton()
                .setText("Добавить количество")
                .setCallbackData(CartHandler.PUSH_TO_CART_CALLBACK_PREFIX + product.getId()));

        final String popMessage,
                popCallbackData;
        if(productCount > 1) {
            popMessage = "Уменьшить количество";
            popCallbackData = CartHandler.POP_FROM_CART_CALLBACK_PREFIX + product.getId();
        } else {
            popMessage = "Удалить из корзины";
            popCallbackData = CartHandler.REMOVE_FROM_CART_CALLBACK_PREFIX + product.getId();
        }
            firstLine.add(new InlineKeyboardButton()
                    .setText(popMessage)
                    .setCallbackData(popCallbackData));

        return markupKeyboard;
    }

    private void addToCart(final long chatId, final String callbackQueryId, final int productId, final MessagesManager messagesManager) {
        final String message;
        if(cartService.addProduct(chatId, productId)) {
            message =  "Товар добавлен.";
        } else {
            message = "Товар уже был добавлен.";
        }
        messagesManager.sendCallbackAnswerPopup(callbackQueryId, message);
    }

    private void changeProductCountInCart(final long chatId, final String callbackQueryId, final int productId,
                                          final int messageId, final BiFunction<Long, Integer, Integer> cartFunction,
                                          final MessagesManager messagesManager) {
        final Product product = productService.get(productId);
        final int newCount = cartFunction.apply(chatId, productId);
        final String message;
        if(newCount > 0) {
            message = "Теперь количество данного товара: " + newCount;
            final EditMessageText editMessageText = new EditMessageText()
                    .setChatId(chatId)
                    .setMessageId(messageId)
                    .setParseMode("Markdown")
                    .setText(generateCartItemInfo(product, newCount))
                    .setReplyMarkup(getButtonsForCartView(product, newCount));
            messagesManager.edit(editMessageText);
        } else {
            message = "Товар удален из корзины";
            messagesManager.deleteMessage(chatId, messageId);
            if(cartService.getCart(chatId).isEmpty()) {
                messagesManager.sendText(chatId, "Корзина пуста");
            }
        }
        messagesManager.sendCallbackAnswerPopup(callbackQueryId, message);
    }

    private int getProductId(final String callbackData, final String prefixToRemove) {
        return NumberUtils.toInt(callbackData.substring(prefixToRemove.length()));
    }
}
