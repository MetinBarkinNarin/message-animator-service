package tr.com.example.kafka;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Optional;

/**
 * Created by nozdemir on 27.02.2020.
 */
final class CustomFluxUtil {

    static Flux<String> fileToFlux(String filePath) {
        return Optional.ofNullable(filePath)
                .map(CustomPathUtil::toPath)
                .map(CustomPathUtil::inputStreamSupplier)
                .map(Flux::fromStream)
                .orElse(Flux.empty());
    }

    static Flux<?> setIntervalToFlux(Flux<?> inputFlux, Long interval) {
        return interval <= 0 ?
                inputFlux :
                inputFlux
                        .zipWith(Flux.interval(Duration.ofMillis(interval)))
                        .map(Tuple2::getT1);
    }
}
