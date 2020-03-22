package com.hairizma.service;

import com.hairizma.dto.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    final List<Product> products = new ArrayList<>();
    {
        Product product = new Product();
        product.setId(1);
        product.setName("Айфон");
        product.setDescription("Лучший телефон в мире");
        products.add(product);

        product = new Product();
        product.setId(2);
        product.setName("Самсунг");
        product.setDescription("Ну почти лучший");
        products.add(product);

        product = new Product();
        product.setId(3);
        product.setName("Ксиаоми");
        product.setDescription("Ну так себе");
        products.add(product);

        product = new Product();
        product.setId(4);
        product.setName("Додж");
        product.setDescription("Говнище а не телефон");
        products.add(product);

        product = new Product();
        product.setId(5);
        product.setName("Мейзу");
        product.setDescription("Пойдет, но лучше не надо.");
        products.add(product);
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public List<Product> getProducts(Set<Integer> ids) {
        return products.stream().filter(product -> ids.contains(product.getId())).collect(Collectors.toList());
    }

    @Override
    public Product get(final int id) {
        for(Product product : products) {
            if(product.getId() == id) {
                return product;
            }
        }
        return null;
    }

}
