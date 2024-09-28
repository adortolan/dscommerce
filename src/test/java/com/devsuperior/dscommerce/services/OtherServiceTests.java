package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.OrderFactory;
import com.devsuperior.dscommerce.UserFactory;
import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
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

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class OtherServiceTests {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuthService authService;

    private Long existingOrderId, nonExistingOrderId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;

    @BeforeEach
    public void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 2L;

        admin = UserFactory.createCustomAdminUser(1L, "Admin");
        client = UserFactory.createCustomClientUser(2L, "Client");
        order = OrderFactory.createOrder(client);

        orderDTO = new OrderDTO(order);
        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExists() {
        OrderDTO result = orderService.findById(existingOrderId);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(existingOrderId);
        Mockito.verifyNoMoreInteractions(orderRepository);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDTO.getId(), result.getId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findById(nonExistingOrderId);
        });
        Mockito.verify(orderRepository, Mockito.times(1)).findById(nonExistingOrderId);
        Mockito.verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {
        Mockito.doNothing().when(authService).vaidateSelfOrAdmin(any());
        OrderDTO result = orderService.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDTO.getId(), result.getId());
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {
        Mockito.doNothing().when(authService).vaidateSelfOrAdmin(any());
        OrderDTO result = orderService.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDTO.getId(), result.getId());
    }

    @Test
    public void findByIdShouldThrowForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
        Mockito.doThrow(ForbiddenException.class).when(authService).vaidateSelfOrAdmin(any());

        Assertions.assertThrows(ForbiddenException.class, () -> {
            orderService.findById(existingOrderId);
        });
    }
}
