package com.devsuperior.dscommerce.controller;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
//Para cada teste vai fazer o rollback do banco por isso a anotação transactional
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    public void setUp() {
        existingId = 26L;
        nonExistingId = 1000L;
        countTotalProducts = 26L;
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products?page=0&size=12&sort=name"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(countTotalProducts))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Macbook Pro"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("PC Gamer"));
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456", roles = "ADMIN")
    public void updateShouldUpdateResourceWhenIdExists() throws Exception {

        ProductDTO productDTO = new ProductDTO(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png");
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();
        Double expectedPrice = productDTO.getPrice();
        String expectedImageUrl = productDTO.getImgUrl();

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedDescription))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(expectedPrice))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imgUrl").value(expectedImageUrl));
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456", roles = "ADMIN")
    public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        ProductDTO productDTO = new ProductDTO(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png");
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
