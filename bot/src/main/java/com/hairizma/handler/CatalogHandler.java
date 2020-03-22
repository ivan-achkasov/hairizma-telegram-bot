package com.hairizma.handler;

import com.hairizma.bot.BotUtils;
import com.hairizma.bot.MainBot;
import com.hairizma.bot.MessagesSender;
import com.hairizma.dto.Product;
import com.hairizma.handler.mapping.MessageTextMapping;
import com.hairizma.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Handler(MainBot.class)
@MessageTextMapping("Каталог")
public class CatalogHandler implements UpdateHandler{

    @Value("${services.host}")
    private String servicesHost;

    private final ProductService productService;

    public CatalogHandler(final ProductService productService) {
        this.productService = Objects.requireNonNull(productService);
    }

    @Override
    public void handleUpdate(final Update update, final MessagesSender messagesSender) throws Exception {
        final long chatId = BotUtils.getChatId(update);
        final List<Product> products = productService.getProducts();
        for (Product product : products) {
            final SendMessage sendMessage = new SendMessage(chatId, generateMessage(product));
            sendMessage.setReplyMarkup(getButtons(product));
            sendMessage.setParseMode("Markdown");
            messagesSender.send(sendMessage);
        }
    }

    private ReplyKeyboard getButtons(final Product product){
        final InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        markupKeyboard.setKeyboard(buttons);

        final List<InlineKeyboardButton> firstLine = new ArrayList<>();
        buttons.add(firstLine);

        firstLine.add(new InlineKeyboardButton()
                .setText("Добавить в корзину")
                .setCallbackData(CartHandler.ADD_TO_CART_CALLBACK_PREFIX + product.getId()));

        return markupKeyboard;
    }

    private String generateMessage(final Product product) {
        String message = "*" + product.getName() + "*\n";
        message += product.getDescription();
        if(product.getImageId() > 0) {
            message += BotUtils.getHiddenImageLink(servicesHost, product.getImageId());
        }
        return message;
    }
}
