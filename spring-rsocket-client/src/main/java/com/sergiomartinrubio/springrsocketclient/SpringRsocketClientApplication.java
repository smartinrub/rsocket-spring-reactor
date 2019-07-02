package com.sergiomartinrubio.springrsocketclient;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@SpringBootApplication
public class SpringRsocketClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRsocketClientApplication.class, args);
    }

    @Bean
    RSocket rSocket() {
        return RSocketFactory
                .connect()
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON_VALUE)
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .transport(TcpClientTransport.create(8803))
                .start()
                .block();
    }

    @Bean
    RSocketRequester requester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.wrap(this.rSocket(), MimeTypeUtils.APPLICATION_JSON, rSocketStrategies);
    }

}

@RestController
@RequiredArgsConstructor
class GreetingRestController {

    private final RSocketRequester requester;

    @GetMapping("/greet/{name}")
    Publisher<GreetingResponse> greet(@PathVariable String name) {
        return this.requester
                .route("greet")
                .data(new GreetingRequest(name))
                .retrieveMono(GreetingResponse.class);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingRequest {
    private String name;
}
@Data
class GreetingResponse {

    private String greeting;

    GreetingResponse() { }

    GreetingResponse(String name) {
        withGreeting("hello" + name + " @ " + Instant.now());
    }
    GreetingResponse withGreeting(String message) {
        this.greeting = message;
        return this;
    }

}
