package com.sanchez.productsapi.models;

public record Reply<T>(ReplyStatus status, String message, T body) {
}
