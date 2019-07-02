package com.sergiomartinrubio.springrsocketserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@SpringBootApplication
public class SpringRsocketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRsocketServerApplication.class, args);
    }
}

@Controller
class GreetingRSocketController {
    @MessageMapping("greet")
    GreetingResponse greet(GreetingRequest request) {
        return new GreetingResponse(request.getName());
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
