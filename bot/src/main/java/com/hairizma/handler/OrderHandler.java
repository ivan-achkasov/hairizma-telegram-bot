package com.hairizma.handler;

import com.hairizma.bot.MainBot;
import com.hairizma.bot.MessagesManager;
import com.hairizma.dto.CustomerDeliveryInfo;
import com.hairizma.handler.mapping.MessageTextMapping;
import com.hairizma.service.CustomerService;
import com.hairizma.service.cart.CartService;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;
import java.util.function.Consumer;

@Handler(MainBot.class)
@MessageTextMapping(OrderHandler.PLACE_ORDER_QUERY)
public class OrderHandler implements UpdateHandler {

    public static final String PLACE_ORDER_QUERY = "Оформить заказ";

    private final CustomerService customerService;
    private final CartService cartService;
    private final HandlerManager handlerManager;

    public OrderHandler(final CustomerService customerService,
                        final CartService cartService,
                        final HandlerManager handlerManager) {
        this.customerService = Objects.requireNonNull(customerService);
        this.cartService = Objects.requireNonNull(cartService);
        this.handlerManager = Objects.requireNonNull(handlerManager);
    }

    @Override
    public void handleUpdate(final Update update, final MessagesManager messagesManager) throws Exception {
        if(update.hasMessage()) {
            handleMessage(update.getMessage(), messagesManager);
        }
    }

    private void handleMessage(final Message message, final MessagesManager messagesManager) throws TelegramApiException {
        final String messageText = message.getText();
        final long chatId = message.getChatId();
        if(PLACE_ORDER_QUERY.equals(messageText)) {
            if(cartService.getCart(chatId).isEmpty()) {
                 messagesManager.sendText( chatId, "Корзина пуста");
            } else {
                checkCustomerData(chatId, messagesManager);
            }
        }
    }

    private void checkCustomerData(final long chatId, final MessagesManager messagesManager) throws TelegramApiException {
        final CustomerDeliveryInfo deliveryInfo = customerService.getOrCreateCustomerInfo(chatId);

        checkCustomerData(chatId, messagesManager, deliveryInfo);
    }

    private void checkCustomerData(final long chatId, final MessagesManager messagesManager, final CustomerDeliveryInfo deliveryInfo) throws TelegramApiException {
        if (StringUtils.isBlank(deliveryInfo.getName())) {
            messagesManager.sendText(chatId, "Введите ваше имя:");
            handlerManager.fixHandleConsumer(chatId, (m, mm) -> {
                try {
                    this.updateName(m, mm);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (StringUtils.isBlank(deliveryInfo.getPhone())) {
            messagesManager.sendText(chatId, "Введите ваш номер телефона:");
            handlerManager.fixHandleConsumer(chatId, (m, mm) -> {
                try {
                    this.updatePhone(m, mm);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (StringUtils.isBlank(deliveryInfo.getDeliveryAddress())) {
            messagesManager.sendText(chatId, "Введите город и отделение новой почты:");
            handlerManager.fixHandleConsumer(chatId, (m, mm) -> {
                try {
                    this.updateDeliveryAddress(m, mm);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            cartService.getCart(chatId).clear();
            messagesManager.sendText(chatId, "Спасибо! Ваш заказ обрабатывается.");
        }
    }

    private void updateField(final Message message, final MessagesManager messagesManager,
                             final Consumer<CustomerDeliveryInfo> update) throws TelegramApiException {
        final long chatId = message.getChatId();
        final CustomerDeliveryInfo deliveryInfo = customerService.getOrCreateCustomerInfo(chatId);
        update.accept(deliveryInfo);
        customerService.saveCustomerInfo(deliveryInfo);
        checkCustomerData(chatId, messagesManager, deliveryInfo);
    }

    private void updateName(final Message message, final MessagesManager messagesManager) throws TelegramApiException {
        updateField(message, messagesManager, d -> d.setName(message.getText()));
    }

    private void updatePhone(final Message message, final MessagesManager messagesManager) throws TelegramApiException {
        updateField(message, messagesManager, d -> d.setPhone(message.getText()));
    }

    private void updateDeliveryAddress(final Message message, final MessagesManager messagesManager) throws TelegramApiException {
        updateField(message, messagesManager, d -> d.setDeliveryAddress(message.getText()));
    }

}
