package com.sanchez.productsapi.models;

public record Commands<T>(String type, Long id, T body) {
}
