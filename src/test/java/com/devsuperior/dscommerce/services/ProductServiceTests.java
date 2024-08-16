package com.devsuperior.dscommerce.services;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DataBaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    private void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        dependentId = 3L;
        Mockito.doThrow(DataBaseException.class).when(repository).deleteById(dependentId);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));
    }

    @Test
    public void deleteShouldBeExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
    }

    @Test
    public void deleteShouldBeExceptionWhenDependentId() {
        Assertions.assertThrows(DataBaseException.class, () -> service.delete(dependentId));
    }
}
