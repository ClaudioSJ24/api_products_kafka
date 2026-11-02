package com.sanchez.productsapi.models;

public record Reply<T>(String status, String message, T body) {
}
