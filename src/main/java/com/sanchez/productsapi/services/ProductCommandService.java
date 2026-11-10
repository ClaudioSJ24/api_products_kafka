package com.sanchez.productsapi.services;

import com.sanchez.productsapi.models.Reply;
import com.sanchez.productsapi.models.dto.ProductDto;

import java.time.Duration;

public interface ProductCommandService {

    Reply<?> sendCreateCommandAndAwait(ProductDto productDto, Duration duration);
   Reply<?> sendUpdateCommandAndAwait(ProductDto productDto, Long id, Duration duration);
    Reply<?> sendDeleteCommandAndAwait(Long id, Duration duration);
    Reply<?> sendGetCommandAndAwait(Long id, Duration duration);
    Reply<?> sendGetAllCommandAndAwait(Duration duration);
}
