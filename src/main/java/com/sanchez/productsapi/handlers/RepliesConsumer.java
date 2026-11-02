package com.sanchez.productsapi.handlers;

import com.sanchez.productsapi.messaging.ReplayInbox;
import com.sanchez.productsapi.models.Reply;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class RepliesConsumer {

    private final ReplayInbox replayInbox;

    public RepliesConsumer(ReplayInbox replayInbox) {
        this.replayInbox = replayInbox;
    }



    @Bean
    public Consumer<Message<Reply<?>>> repliesHandlers() {
        return message -> {
          String correlationId = message.getHeaders().get("correlationId", String.class);
          replayInbox.unregister(correlationId, message.getPayload());
        };
    }
}
