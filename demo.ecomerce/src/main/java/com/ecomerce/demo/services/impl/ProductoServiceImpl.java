package com.ecomerce.demo.services.impl;

import com.ecomerce.demo.dtos.ProductoDTO;
import com.ecomerce.demo.entities.Producto;
import com.ecomerce.demo.exception.CustomException;
import com.ecomerce.demo.repositories.ProductoRepository;
import com.ecomerce.demo.services.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor //not Autowired
public class ProductoServiceImpl implements ProductoService {

    private final static String NF_MESSAGE = "producto no existe";
    private final static String NOMBRE_MESSAGE = "nombre del producto ya en uso";

    private final ProductoRepository productoRepository;

    public Flux<Producto> getAll() {
        return productoRepository.findAll();
    }

    public Mono<Producto> getById(Long id){
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND,NF_MESSAGE)));
    }

    public Mono<Producto> save(ProductoDTO dto) {
        Mono<Boolean> existsNombre = productoRepository.findByNombre(dto.getNombre()).hasElement();
        return existsNombre.flatMap(exists -> exists ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST,NOMBRE_MESSAGE))
                : productoRepository
                .save(Producto
                        .builder()
                        .nombre(dto.getNombre())
                        .descripcion(dto.getDescripcion())
                        .precio(dto.getPrecio())
                        .stock(dto.getStock())
                        .build()));
    }

    public Mono<Producto> update(Long id, ProductoDTO dto) {
        Mono<Boolean> productoId = productoRepository.findById(id).hasElement();
        Mono<Boolean> productoRepeatedNombre = productoRepository.repeatedNombre(id, dto.getNombre()).hasElement();
        return productoId.flatMap(
                existsId -> existsId ? // si existe
                    productoRepeatedNombre.flatMap(existsNombre -> existsNombre ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST,NOMBRE_MESSAGE))
                            : productoRepository.save(new Producto(id, dto.getNombre(), dto.getDescripcion(), dto.getPrecio(), dto.getStock())))
        : Mono.error(new CustomException(HttpStatus.NOT_FOUND,NF_MESSAGE)));
    }

    public Mono<Void> delete(Long id) {
        Mono<Boolean> productoId = productoRepository.findById(id).hasElement();
        return productoId.flatMap(exists -> exists ? productoRepository.deleteById(id) : Mono.error(new CustomException(HttpStatus.NOT_FOUND,NF_MESSAGE)));
    }
}
