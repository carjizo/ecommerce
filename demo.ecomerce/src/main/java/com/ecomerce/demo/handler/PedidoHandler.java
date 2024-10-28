package com.ecomerce.demo.handler;

import com.ecomerce.demo.dtos.PedidoDTO;
import com.ecomerce.demo.dtos.PedidoDetalladoDTO;
import com.ecomerce.demo.entities.Pedido;
import com.ecomerce.demo.services.PedidoService;
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
public class PedidoHandler {

    private final PedidoService pedidoService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        Flux<Pedido> pedidos = pedidoService.getAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(pedidos, Pedido.class);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<PedidoDTO> dtoMono = serverRequest.bodyToMono(PedidoDTO.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(pedidoDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pedidoService.save(pedidoDTO), Pedido.class));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<PedidoDTO> dtoMono = serverRequest.bodyToMono(PedidoDTO.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(pedidoUpdateDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pedidoService.update(id, pedidoUpdateDTO), Pedido.class));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(pedidoService.delete(id), Pedido.class);
    }

    public Mono<ServerResponse> getOne(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        Mono<PedidoDetalladoDTO> pedidoDetalladoDTO = pedidoService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(pedidoDetalladoDTO, Pedido.class);
    }
}
