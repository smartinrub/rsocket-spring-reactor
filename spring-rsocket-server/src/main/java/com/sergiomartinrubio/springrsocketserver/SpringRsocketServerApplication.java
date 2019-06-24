package com.sergiomartinrubio.springrsocketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.StringDecoder;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringRsocketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRsocketServerApplication.class, args);
    }

    @Bean
    RSocketStrategiesCustomizer strategiesCustomizer() {
        return strategies -> strategies
                .decoder(StringDecoder.allMimeTypes())
                .encoder(CharSequenceEncoder.allMimeTypes());
    }

    @Controller
    class ServerController {

        @MessageMapping("request.stream")
        Flux<String> requestStream(String payload) {
            return Mono.just(Integer.valueOf(payload))
                    .doOnNext(i -> System.out.printf("Received: %d%n", i))
                    .flatMapMany(i -> Flux.range(0, i))
                    .doOnNext(i -> System.out.printf("Sending: %d%n", i))
                    .map(String::valueOf);
        }
    }

}
