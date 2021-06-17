package tr.com.example.kafka;

import lombok.extern.java.Log;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

@Log
final class ProgressLoggerUtil {
    private static final String formatWithKey = "[Time] = {0} | [Data Type] = {1} | [Topic] = {2} | [Key] = {3} | [Index] = {4}";
    private static final String format = "[Time] = {0} | [Data Type] = {1} | [Topic] = {2} | [Index] = {3}";
    private static AtomicLong atomicLong = new AtomicLong(0);

    static void logProgress(String dataType, String topicName, Integer key) {
        long index = atomicLong.getAndIncrement();
        if (index % 1000 == 0) {
            String message = Optional.ofNullable(key)
                    .map(k -> MessageFormat.format(
                            ProgressLoggerUtil.formatWithKey,
                            Instant.now(),
                            dataType,
                            topicName,
                            k,
                            index)
                    )
                    .orElse(MessageFormat.format(
                            ProgressLoggerUtil.format,
                            Instant.now(),
                            dataType,
                            topicName,
                            index)
                    );
            log.log(Level.INFO, message);
        }
    }
}
