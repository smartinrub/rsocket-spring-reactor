import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DefaultSimpleService extends AbstractRSocket {

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        return Mono.just(Integer.valueOf(payload.getDataUtf8()))
                .doOnNext(i -> System.out.printf("Receive: %d%n", i))
                .flatMapMany(i -> Flux.range(0, i))
                .doOnNext(i -> System.out.printf("Sending: %d%n", i))
                .map(i -> DefaultPayload.create(String.valueOf(i)));
    }
}
