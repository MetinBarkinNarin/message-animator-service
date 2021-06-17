package tr.com.example.kafka;

import reactor.core.Exceptions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

final class CustomPathUtil {
    static Path toPath(String fileName) {
        if (Files.exists(Paths.get(fileName)))
            return Paths.get(fileName);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = Objects.requireNonNull(classLoader.getResource(fileName));
        return new File(url.getFile()).toPath();
    }

    static Supplier<Stream<? extends String>> inputStreamSupplier(Path path) {
        return () -> {
            try {
                return Files.lines(path);
            } catch (IOException e) {
                throw Exceptions.propagate(e);
            }
        };
    }

    static String getFileAsString(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }
}
