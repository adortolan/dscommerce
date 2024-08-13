package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.OrderStatus;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Instant momment;
    private OrderStatus status;
    private ClientDTO client;
    private PaymentDTO payment;
    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO(Long id, Instant momment, OrderStatus status, ClientDTO client, PaymentDTO payment) {
        this.id = id;
        this.momment = momment;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }

    public OrderDTO(Order entity) {
        id = entity.getId();
        momment = entity.getMoment();
        status = entity.getStatus();
        client = new ClientDTO(entity.getClient());
        payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());

        for (OrderItem item : entity.getItems()) {
            OrderItemDTO dto = new OrderItemDTO(item);
            items.add(dto);
        }
    }

    public Long getId() {
        return id;
    }

    public Instant getMomment() {
        return momment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public Double getTotal() {
        double sum = 0.0;
        for (OrderItemDTO item : items) {
            sum += item. getSubTotal();
        }
        return sum;
    }
}
