package com.hairizma.handler;

import com.hairizma.bot.MainBot;
import com.hairizma.bot.MessagesSender;
import com.hairizma.handler.mapping.MessageTextMapping;
import com.hairizma.service.ProductService;
import org.apache.cxf.common.util.ClassHelper;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Handler(MainBot.class)
@MessageTextMapping("Каталог")
public class CatalogHandler implements UpdateHandler{

    private final ProductService productService;

    public CatalogHandler(final ProductService productService) {
        this.productService = Objects.requireNonNull(productService);
    }

    @Override
    public void handleUpdate(Update update, MessagesSender messagesSender) throws Exception {
        messagesSender.sendText(update.getMessage().getChatId(), productService.getProducts().get(0).getName());
    }
}
