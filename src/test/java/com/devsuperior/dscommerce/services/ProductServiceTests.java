package com.devsuperior.dscommerce.services;
import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DataBaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
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
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    private void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        dependentId = 3L;
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);

        page = new PageImpl<>(List.of(new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png")));
        Mockito.when(repository.searchByName(ArgumentMatchers.anyString(),ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(page.iterator().next());
        product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png");
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
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

    @Test
    public void insertShouldReturnProductDTO() {
        ProductDTO result = service.insert(new ProductDTO(null, "Phone", "Good phone", 800.0, "https://img.com/img.png"));
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldBeExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, new ProductDTO(null, "Phone", "Good phone", 800.0, "https://img.com/img.png")));
    }

    @Test
    public void updateShouldReturnProductDTO() {
        ProductDTO dto = new ProductDTO(1L, "Updated Product", "Updated Description", 900.0, "https://img.com/img2.png");
        ProductDTO result = service.update(existingId, dto);
        Assertions.assertNotEquals(dto, result);
        Mockito.verify(repository).save(ArgumentMatchers.any(Product.class));
    }

    @Test
    public void findByIdShouldReturnProductDTO() {
        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldBeExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
    }

    @Test
    public void findAllShouldReturnPagedProductMinDTO() {
        Page<ProductMinDTO> result = service.findAll("Phone", PageRequest.of(0, 10));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(result.iterator().next().getName(), "Phone");
    }
}

