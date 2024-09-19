package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.CategoryFactory;
import com.devsuperior.dscommerce.dto.CategoryDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    private Category category;
    private List<Category> list;

    @BeforeEach
    public void setUp() {
        category = CategoryFactory.createCategory();

        list = new ArrayList<>();
        list.add(category);

        Mockito.when(repository.findAll()).thenReturn(list);
    }

    @Test
    public void findAllShouldReturnListCategoryDTO() {
        List<CategoryDTO> list = service.findAll();

        Mockito.verify(repository, Mockito.times(1)).findAll();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(list.get(0).getId(), category.getId());
        Assertions.assertEquals(list.get(0).getName(), category.getName());
    }
}
