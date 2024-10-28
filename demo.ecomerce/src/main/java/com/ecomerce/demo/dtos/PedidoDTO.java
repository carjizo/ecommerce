package com.ecomerce.demo.dtos;

import com.ecomerce.demo.entities.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PedidoDTO {

    private Long clienteId;
    private List<Pedido.ProductoCantidad> productos;
    private Double total;
    private LocalDateTime fechaPedido;

    @Data
    public static class ProductoCantidad {
        private Long productoId;
        private Integer cantidad;
    }
}
