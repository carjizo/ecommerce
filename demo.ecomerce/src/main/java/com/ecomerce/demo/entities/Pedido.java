package com.ecomerce.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "pedidos")
@Builder
public class Pedido {

    @Id
    private String id;
    private Long clienteId;
    private List<ProductoCantidad> productos;
    private Double total;
    private LocalDateTime fechaPedido;

    @Data
    public static class ProductoCantidad {
        private Long productoId;
        private Integer cantidad;
    }
}
