package com.devsuperior.dscommerce.controller;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.ProductService;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
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

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private PageImpl<ProductMinDTO> page;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    private void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        page = new PageImpl<>(List.of(new ProductMinDTO (1L, "Phone",  800.0, "https://img.com/img.png")));
        Mockito.when(productService.findAll(ArgumentMatchers.anyString(),ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(productService.findById(existingId)).thenReturn(new ProductDTO(1L, "Phone", "Good phone",
                800.0, "https://img.com/img.png"));
        Mockito.when(productService.findById(nonExistingId)).thenThrow(new ResourceNotFoundException("Recurso não encontrado"));

        Product product = new Product(1l, "Phone", "Good phone", 800.0, "https://img.com/img.png");

        Mockito.when(productService.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(
                new ProductDTO(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png"));
        Mockito.when(productService.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(new ResourceNotFoundException("Recurso não encontrado"));
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId)
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", nonExistingId)
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTO() throws Exception {

        ProductDTO dto = new ProductDTO(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png");
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                        .content(json)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateShouldReturnResourceNotFoundException() throws Exception {

        ProductDTO dto = new ProductDTO(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png");
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                        .content(json)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
