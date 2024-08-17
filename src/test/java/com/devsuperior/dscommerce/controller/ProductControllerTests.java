package com.devsuperior.dscommerce.controller;

import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.services.ProductService;
import org.assertj.core.internal.Classes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProductsController.class)
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ProductService productService;

    private PageImpl<ProductMinDTO> page;

    @BeforeEach
    private void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        page = new PageImpl<>(List.of(new ProductMinDTO (1L, "Phone",  800.0, "https://img.com/img.png")));
        Mockito.when(productService.findAll(ArgumentMatchers.anyString(),ArgumentMatchers.any())).thenReturn(page);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
