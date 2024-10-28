//package com.ecomerce.demo.controllers;
//
//import com.ecomerce.demo.entities.Producto;
//import com.ecomerce.demo.services.ProductoService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/producto")
//@Slf4j
//@RequiredArgsConstructor
//public class ProductoController {
//
//    private final ProductoService productoService;
//
//    @GetMapping
//    public Flux<Producto> getAll(){
//        return productoService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public Mono<Producto> getById(@PathVariable("id") Long id){
//        return productoService.getById(id);
//    }
//
//    @PostMapping
//    public Mono<Producto> save(@RequestBody Producto producto) {
//        return productoService.save(producto);
//    }
//
//    @PutMapping("/{id}")
//    public Mono<Producto> update(@PathVariable("id") Long id, @RequestBody Producto producto){
//        return productoService.update(id, producto);
//    }
//
//    @DeleteMapping("/{id}")
//    public Mono<Void> delete(@PathVariable("id") Long id){
//        return productoService.delete(id);
//    }
//}
