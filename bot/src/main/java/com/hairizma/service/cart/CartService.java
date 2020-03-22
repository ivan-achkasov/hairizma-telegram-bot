package com.hairizma.service.cart;

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

    public int pushProduct(final long chatId, final int productId) {
        return getCart(chatId).pushProduct(productId);
    }

    public int popProduct(final long chatId, final int productId) {
        return getCart(chatId).popProduct(productId);
    }

    public boolean addProduct(final long chatId, final int productId) {
        return getCart(chatId).addProduct(productId);
    }

    public void removeProduct(final long chatId, final int productId) {
        getCart(chatId).removeProduct(productId);
    }
}
