package com.sanchez.productsapi.models;

public record Commands<T>(CommandType type, Long id, T body) {
}
