package com.ecomerce.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("clientes")
@Builder
public class Cliente {

    @Id
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
}
