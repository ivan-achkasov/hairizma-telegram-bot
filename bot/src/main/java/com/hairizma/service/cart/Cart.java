package com.hairizma.service.cart;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    final long chatId;
    final Map<Integer, Integer> productsCount = new LinkedHashMap<>();

    Cart(long chatId) {
        this.chatId = chatId;
    }

    public synchronized int pushProduct(final int productId) {
        final Integer count = productsCount.get(productId);
        final int newCount = (count == null ? 0 : count) + 1;

        productsCount.put(productId, newCount);

        return newCount;
    }

    public synchronized int popProduct(final int productId) {
        final Integer count = productsCount.get(productId);
        final int newCount = (count == null ? 0 : count) - 1;

        if(newCount > 0) {
            productsCount.put(productId, newCount);
            return newCount;
        } else {
            removeProduct(productId);
        }
        return 0;
    }

    public synchronized void removeProduct(final int productId) {
        productsCount.remove(productId);
    }

    public synchronized boolean addProduct(int productId) {
        return productsCount.putIfAbsent(productId, 1) == null;
    }

    public synchronized Map<Integer, Integer> getProductsCount() {
        return new LinkedHashMap<>(productsCount);
    }

    public synchronized void clear() {
        productsCount.clear();
    }

    public synchronized boolean isEmpty() {
        return productsCount.isEmpty();
    }
}
