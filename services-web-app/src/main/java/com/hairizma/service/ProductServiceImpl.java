package com.hairizma.service;

import com.hairizma.dto.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<Product> getProducts() {
        final Product product0 = new Product();
        product0.setId(4649);
        product0.setName("nameeee");
        product0.setDescription("descrrrr");

        final Product product1 = new Product();
        product1.setId(2);
        product1.setName("awdawdaw1");

        return Arrays.asList(product0, product1);
    }

}
