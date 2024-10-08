package com.devsuperior.dscommerce.controller;

import com.devsuperior.dscommerce.OrderFactory;
import com.devsuperior.dscommerce.ProductFactory;
import com.devsuperior.dscommerce.UserFactory;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
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
public class OrderControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private Order order;
    private User user;


    @BeforeEach
    public void setUp() throws Exception {
        user = UserFactory.createAdmintUser();
        order = OrderFactory.createOrder(user);
    }

    @Test
    @WithMockUser(username = "adortolan@gmail.com", password = "123456", roles = "ADMIN")
    public void findByIdOrderShouldReturnOrderDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/4"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.client.name").value("Alex"));
    }
}
