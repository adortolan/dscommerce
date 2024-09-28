package com.devsuperior.dscommerce;

import com.devsuperior.dscommerce.entities.Product;

public class ProductFactory {
    public static Product createProduct() {

        return new Product(1L, "Product 1", "Description 1", 10.0, "https://img.com.br");
    }
}
