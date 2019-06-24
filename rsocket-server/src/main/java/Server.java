import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import reactor.core.publisher.Mono;

public class Server {

    public static void main(String[] args) {
        RSocketFactory.receive()
                .acceptor(((connectionSetupPayload, rSocket) -> Mono.just(new DefaultSimpleService())))
                .transport(WebsocketServerTransport.create(8801))
                .start()
                .block()
                .onClose()
                .block();
    }

}
