package com.sanchez.productsapi.controllers;

import com.sanchez.productsapi.models.Reply;
import com.sanchez.productsapi.models.dto.ProductDto;
import com.sanchez.productsapi.services.ProductCommandService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        if ("SUCCESS".equalsIgnoreCase(reply.status())) {
            return ResponseEntity.ok(reply.body());
        }
        return ResponseEntity.badRequest().body(Map.of("ERROR", reply.message()));
    }
}
