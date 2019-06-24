import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;

public class Client {

    public static void main(String[] args) {
        RSocket rSocket = RSocketFactory.connect()
                .transport(WebsocketClientTransport.create(8801))
                .start()
                .block();

        Flux.range(0, 10)
                .doOnNext(i -> System.out.printf("Sending: %d%n", i))
                .map(i -> DefaultPayload.create(String.valueOf(i)))
                .flatMap(message -> rSocket.requestStream(message))
                .doOnNext(response -> System.out.printf("Received: %s%n", response.getDataUtf8()))
                .blockLast();
    }
}
