package com.ecomerce.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClienteDTO {

    @NotBlank(message = "nombre es obligartorio")
    private String nombre;
    @NotBlank(message = "email es obligatorio")
    private String email;
    @NotBlank(message = "telefono es obligatorio")
    private String telefono;
    @NotBlank(message = "direccion es obligatorio")
    private String direccion;
}
