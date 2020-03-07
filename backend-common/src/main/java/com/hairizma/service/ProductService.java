package com.hairizma.service;

import com.hairizma.dto.Product;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/product")
public interface ProductService {

    @RequestMapping("/all")
    Product[] getProducts();

}
