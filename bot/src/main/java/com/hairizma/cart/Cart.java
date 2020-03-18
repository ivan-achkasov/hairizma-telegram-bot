package com.hairizma.cart;

import com.hairizma.dto.Product;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    final long chatId;
    final Map<Integer, Integer> productsCount = new LinkedHashMap<>();

    Cart(long chatId) {
        this.chatId = chatId;
    }

    public synchronized void pushProduct(final int productId) {
        final Integer count = productsCount.get(productId);
        final int newCount = (count == null ? 0 : count) + 1;

        productsCount.put(productId, newCount);
    }

    public synchronized void popProduct(final int productId) {
        final Integer count = productsCount.get(productId);
        final int newCount = (count == null ? 0 : count) - 1;

        if(newCount >= 0) {
            productsCount.put(productId, newCount);
        }
    }

    public synchronized void clear() {
        productsCount.clear();
    }

    public synchronized boolean addProduct(int productId) {
        return productsCount.putIfAbsent(productId, 0 ) == null;
    }
}
