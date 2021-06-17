package tr.com.example.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

/**
 * Created by nozdemir on 17.02.2020.
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class SendingDataProps extends DefaultDataProps {
    private Long times;
    private Long interval;
    private Integer workerNumber;
    private Integer key;
    private String topicName;
    private String fileName;
    private SendingType sendingType;
    private String schemaFileName;

    public Long getTimes() {
        return Optional.ofNullable(times)
                .orElse(DEFAULT_TIMES);
    }

    public Long getInterval() {
        return Optional.ofNullable(interval)
                .orElse(DEFAULT_INTERVAL);
    }

    public Integer getWorkerNumber() {
        return Optional.ofNullable(workerNumber)
                .orElse(DEFAULT_WORKER_NUMBER);
    }

    public Integer getKey() {
        return Optional.ofNullable(key)
                .orElse(DEFAULT_KEY);
    }

    public String getTopicName() {
        return Optional.ofNullable(topicName)
                .orElse(DEFAULT_TOPIC_NAME);
    }

    public String getFileName() {
        return Optional.ofNullable(fileName)
                .orElse(DEFAULT_FILE_NAME);
    }

    public SendingType getSendingType() {
        return Optional.ofNullable(sendingType)
                .orElse(DEFAULT_SENDING_TYPE);
    }

    public String getSchemaFileName() {
        return Optional.ofNullable(schemaFileName)
                .orElse(DEFAULT_SCHEMA_FILE_NAME);
    }
}
