package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DataBaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private Long dependentId;

    @BeforeEach
    public void setUp() {
        existingId = 26L;
        nonExistingId = 1000L;
        countTotalProducts = 26L;
        dependentId = 3L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);

        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    public void deleteShouldBeExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldBeExceptionWhenIdIsNull() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void findAllShouldReturnAllProducts() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<ProductMinDTO> result = service.findAll("", pageable);
        Assertions.assertNotEquals(0, result.getTotalElements());
    }

    @Test
    public void findAllShouldReturnPageSorted() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductMinDTO> result = service.findAll("", pageable);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
    }

    @Test
    public void findAllShouldReturnPageSortedByPrice() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("price"));
        Page<ProductMinDTO> result = service.findAll("", pageable);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(90.5, result.getContent().get(0).getPrice());
        Assertions.assertEquals(100.99, result.getContent().get(1).getPrice());
    }
}
