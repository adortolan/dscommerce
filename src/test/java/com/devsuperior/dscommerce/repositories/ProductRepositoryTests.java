package com.devsuperior.dscommerce.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repository;
    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        long existingId = 1L;

        repository.deleteById(existingId);

        Assertions.assertTrue(repository.findById(existingId).isEmpty());
    }
}
