package com.sanchez.productsapi.controllers;

import com.sanchez.productsapi.models.Reply;
import com.sanchez.productsapi.models.ReplyStatus;
import com.sanchez.productsapi.models.dto.ProductDto;
import com.sanchez.productsapi.services.ProductCommandService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductCommandService productCommandService;

    public ProductController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
        Reply<?> reply = this.productCommandService.sendCreateCommandAndAwait(productDto, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
       return getResponseEntity(this.productCommandService.sendGetCommandAndAwait(id, Duration.ofSeconds(5)));
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        Reply<?> reply = this.productCommandService.sendGetAllCommandAndAwait(Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto){
         return getResponseEntity(this.productCommandService.sendUpdateCommandAndAwait(productDto, id, Duration.ofSeconds(5)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        Reply<?> reply = this.productCommandService.sendDeleteCommandAndAwait(id, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }
    private static @NonNull ResponseEntity<?> getResponseEntity(Reply<?> reply) {
        if ( reply.status().isSuccess()) {
            return ResponseEntity.ok(reply.body());
        }
        return ResponseEntity.badRequest().body(Map.of("ERROR", reply.message()));
    }

}
