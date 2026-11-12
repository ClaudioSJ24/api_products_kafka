package com.sanchez.productsapi.models;

public enum ReplyStatus {
    SUCCESS,
    ERROR;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
