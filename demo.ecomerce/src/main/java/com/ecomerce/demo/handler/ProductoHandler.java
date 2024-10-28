package com.ecomerce.demo.handler;

import com.ecomerce.demo.dtos.ProductoDTO;
import com.ecomerce.demo.entities.Producto;
import com.ecomerce.demo.services.ProductoService;
import com.ecomerce.demo.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductoHandler {

    private final ProductoService productoService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> getAll(ServerRequest serverRequest){
        Flux<Producto> productos = productoService.getAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productos, Producto.class);
    }

    public Mono<ServerResponse> getOne(ServerRequest serverRequest){
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        Mono<Producto> producto = productoService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(producto, Producto.class);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<ProductoDTO> dtoMono = serverRequest.bodyToMono(ProductoDTO.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(productoDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productoService.save(productoDto), Producto.class));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest){
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        Mono<ProductoDTO> dtoMono = serverRequest.bodyToMono(ProductoDTO.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(productoDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productoService.update(id, productoDto), Producto.class));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest){
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productoService.delete(id), Producto.class);
    }
}
