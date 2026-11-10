package com.sanchez.productsapi.services;

import com.sanchez.productsapi.messaging.ReplayInbox;
import com.sanchez.productsapi.models.Commands;
import com.sanchez.productsapi.models.Reply;
import com.sanchez.productsapi.models.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final StreamBridge streamBridge;
    private final ReplayInbox replayInbox;
    private static final Logger logger = LoggerFactory.getLogger(ProductCommandServiceImpl.class);
    public ProductCommandServiceImpl(StreamBridge streamBridge, ReplayInbox replayInbox) {

        this.streamBridge = streamBridge;
        this.replayInbox = replayInbox;
    }

    @Override
    public Reply<?> sendCreateCommandAndAwait(ProductDto productDto, Duration duration) {
        Commands<ProductDto> cmd = new Commands<>("CREATE", null, productDto);
        return sendAndAwait(duration, cmd);
    }

    @Override
    public Reply<?> sendUpdateCommandAndAwait(ProductDto productDto, Long id, Duration duration) {
        Commands<ProductDto> cmd = new Commands<>("UPDATE", id, productDto);
        return sendAndAwait(duration, cmd);
    }

    @Override
    public Reply<?> sendDeleteCommandAndAwait(Long id, Duration duration) {
        Commands<ProductDto> cmd = new Commands<>("DELETE", id, null);
        return sendAndAwait(duration, cmd);
    }

    @Override
    public Reply<?> sendGetCommandAndAwait(Long id, Duration duration) {
        
        Commands<ProductDto> cmd = new Commands<>("READ", id, null);
        return sendAndAwait(duration, cmd);
    }

    @Override
    public Reply<?> sendGetAllCommandAndAwait(Duration duration) {
        Commands<ProductDto> cmd = new Commands<>("READ_ALL", null, null);
        return sendAndAwait(duration, cmd);
    }

    private Reply<?> sendAndAwait(Duration duration, Commands<ProductDto> cmd) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("API products Client creating product with correlationId={} ", correlationId);
        Message<Commands<ProductDto>> message = MessageBuilder.withPayload(cmd)
                .setHeader("correlationId", correlationId)
                .build();

        CompletableFuture<Reply<?>> future = replayInbox.register(correlationId);

        boolean sent = this.streamBridge.send("name-topic-commands-out-0", message);

        if (!sent) {
            throw new IllegalStateException("Unable to send command kafka");
        }

        try {
            return future.get(duration.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
