//package com.ecomerce.demo.controllers;
//
//import com.ecomerce.demo.entities.Pedido;
//import com.ecomerce.demo.services.PedidoService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/pedido")
//@RequiredArgsConstructor
//public class PedidoController {
//
//    private final PedidoService pedidoService;
//
//    @PostMapping
//    public Mono<Pedido> crearPedido(@RequestBody Pedido pedido) {
//        return pedidoService.save(pedido);
//    }
//
//    @GetMapping
//    public Flux<Pedido> listarPedidos() {
//        return pedidoService.listarPedidos();
//    }
//}
