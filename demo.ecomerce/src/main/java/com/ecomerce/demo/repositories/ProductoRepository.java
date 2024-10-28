package com.ecomerce.demo.repositories;

import com.ecomerce.demo.entities.Producto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {
    Mono<Producto> findByNombre(String nombre);

    @Query("SELECT * FROM productos WHERE id <> :id AND nombre = :nombre")
    Mono<Producto> repeatedNombre(Long id, String nombre);
}
