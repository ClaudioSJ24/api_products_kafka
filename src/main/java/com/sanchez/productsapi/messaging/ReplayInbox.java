package com.sanchez.productsapi.messaging;

import com.sanchez.productsapi.models.Reply;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReplayInbox {

    private final ConcurrentHashMap<String, CompletableFuture<Reply<?>>> pending = new ConcurrentHashMap<>();

    public CompletableFuture<Reply<?>> register(String correlationId) {
        CompletableFuture<Reply<?>> future = new CompletableFuture<>();
        pending.put(correlationId, future);
        return future;
    }

    public void unregister(String correlationId, Reply<?> reply) {

        if (correlationId == null) {
            throw new NullPointerException("correlationId cannot be null");
        }
        CompletableFuture<Reply<?>> future = pending.remove(correlationId);
        if (future != null) {
            future.complete(reply);
        }
    }
}
