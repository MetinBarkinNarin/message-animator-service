package tr.com.example.kafka;

/**
 * Created by nozdemir on 27.02.2020.
 */
public class DefaultDataProps {
    static final Long DEFAULT_TIMES = 1L;
    static final Long DEFAULT_INTERVAL = 2L;
    static final Integer DEFAULT_WORKER_NUMBER = Runtime.getRuntime().availableProcessors();
    static final Integer DEFAULT_KEY = 101;
    static final String DEFAULT_TOPIC_NAME = "default-topic";
    static final String DEFAULT_FILE_NAME = "";
    static final String DEFAULT_SCHEMA_FILE_NAME = null;
    static final SendingType DEFAULT_SENDING_TYPE = SendingType.JSON;
}
