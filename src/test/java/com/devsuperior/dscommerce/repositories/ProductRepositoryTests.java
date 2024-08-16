package com.devsuperior.dscommerce.repositories;

import com.devsuperior.dscommerce.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repository;

    private Long existingId;
    @BeforeEach
    private void setUp() throws Exception {
        existingId = 1L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);

        Assertions.assertTrue(repository.findById(existingId).isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Product product = new Product(null, "Phone", "Good phone", 800.0, "https://img.com/img.png");
        Product savedProduct = repository.save(product);

        Assertions.assertNotNull(savedProduct.getId());
        Assertions.assertEquals(product.getId(), savedProduct.getId());
    }
}
