package com.hairizma.cart;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private Map<Long, Cart> carts = new HashMap<>();

    public synchronized Cart getCart(final long chatId) {
        carts.putIfAbsent(chatId, new Cart(chatId));
        return carts.get(chatId);
    }

    public void pushProduct(final long chatId, final int productId) {
        getCart(chatId).pushProduct(productId);
    }

    public void popProduct(final long chatId, final int productId) {
        getCart(chatId).popProduct(productId);
    }

    public boolean addProduct(final long chatId, final int productId) {
        return getCart(chatId).addProduct(productId);
    }
}
