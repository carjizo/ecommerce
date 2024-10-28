package com.ecomerce.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PedidoDetalladoDTO {
    private String id;
    private Long clienteId;
    private String clienteNombre;
    private String clienteEmail;
    private String clienteTelefono;
    private String clienteDireccion;
    private List<ProductoDetalle> productos;
    private Double total;
    private LocalDateTime fechaPedido;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductoDetalle {
        private Long productoId;
        private String nombre;
        private String descripcion;
        private Double precio;
//        private Integer cantidad;
    }
}
