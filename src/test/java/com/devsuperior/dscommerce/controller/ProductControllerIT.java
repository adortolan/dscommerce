package com.devsuperior.dscommerce.controller;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private ProductDTO productDTO;

    @BeforeEach
    public void setUp() {
        existingId = 26L;
        nonExistingId = 1000L;
        countTotalProducts = 24L;

        productDTO = new ProductDTO(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png");
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products?page=0&size=12&sort=name"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(countTotalProducts))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("PC Gamer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("PC Gamer Alfa"));

    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456", roles = "ADMIN")
    public void updateShouldUpdateResourceWhenIdExists() throws Exception {

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
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void findAllShouldReturnWhenSortByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products?name=PC Gamer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value("4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("PC Gamer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value("1200.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].imgUrl")
                        .value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/4-big.jpg"));
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456", roles = "ADMIN")
    public void deleteShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", nonExistingId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456", roles = "ADMIN")
    public void insertShouldReturnProdcutDTO() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(jsonBody)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(productDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(productDTO.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imgUrl").value(productDTO.getImgUrl()));
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456", roles = "ADMIN")
    public void insertShouldReturn402WhenProductNameIsInvalid() throws Exception {

        ProductDTO productDTONotName = new ProductDTO(1L, null, "Good phone", 800.0, "https://img.com/img.png");

        String jsonBody = objectMapper.writeValueAsString(productDTONotName);
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(jsonBody)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void insertShouldNotReturnWhenClientAndTokenIsInvalid() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(jsonBody)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
