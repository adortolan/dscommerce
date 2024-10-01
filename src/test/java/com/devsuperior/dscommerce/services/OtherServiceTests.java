package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.OrderFactory;
import com.devsuperior.dscommerce.ProductFactory;
import com.devsuperior.dscommerce.UserFactory;
import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    private Long existingOrderId, nonExistingOrderId;
    private Long existingProductId, nonExistingProductId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;
    private Product product;

    @BeforeEach
    public void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 2L;

        existingProductId = 1L;
        nonExistingProductId = 2L;

        admin = UserFactory.createCustomAdminUser(1L, "Admin");
        client = UserFactory.createCustomClientUser(2L, "Client");
        order = OrderFactory.createOrder(client);

        orderDTO = new OrderDTO(order);
        product = ProductFactory.createProduct();

        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(orderRepository.save(any())).thenReturn(order);
        Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));
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

    @Test
    public void insertShouldReturnOrderDTOWhenAdminLogged() {
        Mockito.when(userService.authenticated()).thenReturn(admin);
        OrderDTO result = orderService.insert(orderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDTO.getId(), result.getId());
    }

    @Test
    public void insertShouldReturnOrderDTOWhenClientAdminLogged() {
        Mockito.when(userService.authenticated()).thenReturn(client);
        OrderDTO result = orderService.insert(orderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDTO.getId(), result.getId());
    }

    @Test
    public void insertShouldThrowUserNameNotFoundExceptionWhenUserNotLogged() {
        Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        order.setClient(new User());
        orderDTO = new OrderDTO(order);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            orderService.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldThrowEntityNotFoundExceptionWhenProductIdDoesNotExists() {
        Mockito.when(userService.authenticated()).thenReturn(client);

        product.setId(nonExistingProductId);
        OrderItem orderItem = new OrderItem(order, product, 2, 10.00);
        order.getItems().add(orderItem);
        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            orderService.insert(orderDTO);
        });
    }
}
