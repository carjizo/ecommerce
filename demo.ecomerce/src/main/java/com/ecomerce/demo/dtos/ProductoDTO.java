package com.ecomerce.demo.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductoDTO {

    @NotBlank(message = "nombre es obligartorio")
    private String nombre;
    private String descripcion;
    @Min(value = 1, message = "precio debe ser mayor a cero")
    private Double precio;
    private Integer stock;
}
