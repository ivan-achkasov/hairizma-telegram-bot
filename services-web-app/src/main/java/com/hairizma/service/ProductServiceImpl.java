package com.hairizma.service;

import com.hairizma.dto.Product;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceImpl implements ProductService {

    @Override
    public Product[] getProducts() {
        final Product[] products = new Product[1];
        final Product product0 = new Product();
        products[0] = product0;
        product0.setId(4649);
        product0.setName("nameeee");
        product0.setDescription("descrrrr");
        return products;
    }

}
